package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule extends RuleImpl {

    public ZScoreRule(double zScoreThreshold, double weight) {
        super(weight, zScoreThreshold);
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double sumTrans = transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .sum();

        double avgTransaction = sumTrans / (transactions.stream()
            .distinct()
            .count());

        int zScore = 

    }

    private double getStandardDeviation(List<Transaction> transactions, double avgTransaction) {
        double sum = transactions.stream()
            .reduce( (avgTransaction, currTrans) -> Transaction::transactionAmount - avgTransaction)
    }
}
