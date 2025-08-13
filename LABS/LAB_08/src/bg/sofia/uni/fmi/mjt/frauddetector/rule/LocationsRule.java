package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule extends RuleImpl {

    public LocationsRule(int threshold, double weight) {
        super(weight, threshold);
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        int countOp = (int)transactions.stream()
            .map(Transaction::location)
            .distinct()
            .count();

        return countOp > super.getThreshold();
    }
}
