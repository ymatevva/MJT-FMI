package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.lang.annotation.Target;
import java.util.List;

public class ZScoreRule extends RuleImpl {

    public ZScoreRule(double zScoreThreshold, double weight) {
        super(weight, zScoreThreshold);
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.size() < 2) {
            return false; // Not enough data to calculate Z-score
        }

        double meanValue = getMeanValue(transactions);
        double variance = getVariance(transactions, meanValue);

        if (variance == 0) {
            return false; // No variation, Z-score meaningless
        }

        double standardDeviation = Math.sqrt(variance);

        return transactions.stream()
            .anyMatch(t -> (t.transactionAmount() - meanValue) / standardDeviation > super.getThreshold());
    }


    private double getVariance(List<Transaction> transactions, double avgTransaction) {

        double sumPowerOfTransactions = transactions.stream()
            .mapToDouble( transaction -> Math.pow(transaction.transactionAmount(), 2))
            .sum();

        double meanValue = sumPowerOfTransactions / (transactions.stream()
            .count());

        return meanValue - Math.pow(getMeanValue(transactions), 2);
    }

    private double getMeanValue(List<Transaction> transactions) {
        double sumTransactions = transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .sum();

        double meanValue = sumTransactions / (transactions.stream()
            .count());

        return meanValue;
    }
}
