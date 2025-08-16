package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private Set<Transaction> transactions;
    private Map<String, Double> accountsRisk;
    private  List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        validateRulesWeight(rules); // the rules total weight should be 1.0
        transactions = new HashSet<>();
        this.rules = rules;
        accountsRisk = new HashMap<>();
        readDataFromFile(reader); // reading the data and transforming the lines to Transaction objects with the .of factory method
      //  calculateRisks(rules); // this method takes account then all its transactions and calculates the risk that this account is not safe
    }


    @Override
    public List<Transaction> allTransactions() {
        return transactions.stream().toList();
    }

    // CHECKED
    @Override
    public List<String> allAccountIDs() { // returns "set" of the account's IDs
        return transactions.stream()
            .map(Transaction::accountID)
            .distinct()
            .toList();
    }

    // CHECKED
    @Override
    public Map<Channel, Integer> transactionCountByChannel() { // grouping by channel and then counting for each channel the number of operations
        return transactions.stream().collect(Collectors.groupingBy(Transaction::channel,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    // CHECKED
    @Override
    public double amountSpentByUser(String accountID) {
        validateAccountId(accountID);

        return transactions.stream()
            .filter(p -> p.accountID().equals(accountID))
            .mapToDouble(Transaction::transactionAmount)
            .sum();
    }

    // CHECKED
    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        validateAccountId(accountId);

        return transactions.stream()
            .filter(p -> p.accountID().equals(accountId))
            .toList();
    }

    // CHECKED
    @Override
    public double accountRating(String accountId) { // fast lookup in the hash map
        validateAccountId(accountId);
        return accountsRisk().get(accountId);
    }

    @Override   // the sorted map will compare based on the risk and if the risks are equal => comparison by account's id
    public SortedMap<String, Double> accountsRisk() {
        calculateRisks(rules);
        SortedMap<String, Double> risks = new TreeMap<>(
            Comparator.<String, Double>comparing(accountsRisk::get)
                .reversed()
                .thenComparing(Comparator.naturalOrder())
        );
        risks.putAll(accountsRisk);
        return risks;
    }

    private void readDataFromFile(Reader reader) { // try with resources
        try (BufferedReader bufferedReader = new BufferedReader(reader); // we declare two resources
             Stream<String> data = bufferedReader.lines()) { // data.skip(n) returns a stream, then we say to that stream that for each object...
             data.skip(1).forEach(line -> { // each line is transformed to a Transaction object
                transactions.add(Transaction.of(line));
            });
        } catch (IOException e) {
            throw new RuntimeException("Exception thrown while trying to read file.");
        }
    }

    // CHECKED
    private void calculateRisks(List<Rule> rules) {
        List<String> accounts = allAccountIDs();

        // this method takes an account and all its transactions,
        // then checks all rules and add the weight of the ones can be applied

        for (int i = 0; i < accounts.size(); i++) {
            double accountRisk = 0;
            List<Transaction> accTr = allTransactionsByUser(accounts.get(i));

            for (var rule : rules) {
                if (rule.applicable(accTr)) {
                    accountRisk += rule.weight();
                }
            }
            accountsRisk.put(accounts.get(i), accountRisk);
        }
    }

    // CHECKED
    private void validateAccountId(String accountID) {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("The accountID is not valid.");
        }
    }

    // CHECKED
    private void validateRulesWeight(List<Rule> rules) {
        double totalWeight = rules.stream().mapToDouble(Rule::weight)
                .sum();

        if (Math.abs(totalWeight - 1.0) > 0.0001) {
            throw new IllegalArgumentException("The rules total weight is not 1.0;");
        }
    }
}
