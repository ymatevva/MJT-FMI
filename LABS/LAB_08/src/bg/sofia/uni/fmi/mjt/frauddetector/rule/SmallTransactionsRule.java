package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule extends RuleImpl {

    // this is the minimum amount that can be withdrawn

    private double amountThreshold;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        super(weight, countThreshold);
        this.amountThreshold = amountThreshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        return transactions.stream()
            .filter(p -> p.transactionAmount() < amountThreshold)
            .count()
            > super.threshold();
    }

}
