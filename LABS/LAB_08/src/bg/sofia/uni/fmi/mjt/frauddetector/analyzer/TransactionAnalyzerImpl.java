package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private List<Transaction> transactions;
    private SortedMap<String, Double> accountsRisk;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {

        validateRulesWeight(rules);
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
            .map(Transaction::accountID)
            .distinct()
            .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream().collect(Collectors.groupingBy(Transaction::channel,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        validateAccountId(accountID);

        double amountSpent = transactions.stream()
            .filter(p -> p.accountID().equals(accountID))
            .mapToDouble(Transaction::transactionAmount)
            .sum();

        return amountSpent;
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        validateAccountId(accountId);

        return transactions.stream()
            .filter(p -> p.accountID().equals(accountId))
            .toList();
    }

    @Override
    public double accountRating(String accountId) {
        validateAccountId(accountId);
        return accountsRisk.get(accountId);
    }

    /**
     * Calculates the risk score for each account based on the loaded rules.
     * The score for each account is a double-precision floating-point number in the interval [0, 1] and is
     * formed by summing up the weights of all applicable rules to the account transactions.
     * The result is sorted in descending order, with the highest-risk accounts listed first.
     *
     * @return a map where keys are account IDs (strings) and values are risk scores (doubles).
     */
    @Override
    public SortedMap<String, Double> accountsRisk() {
        return accountsRisk;
    }

    private void validateAccountId(String accountID) {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("The accountID is not valid.");
        }
    }

    private void validateRulesWeight(List<Rule> rules) {
        double totalWeight = rules.stream().mapToDouble(Rule::weight)
            .reduce((res, el) -> res + el)
            .orElseThrow(IllegalArgumentException::new);
    }
}
