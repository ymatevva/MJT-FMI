package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule extends RuleImpl {

    public LocationsRule(int threshold, double weight) {
        super(weight, threshold);
    }

    // if the operations are made from more than 'threshold' locations => true

     @Override
    public boolean applicable(List<Transaction> transactions) {

        return transactions.stream()
            .collect(Collectors.groupingBy(Transaction::location
                ,Collectors.counting())).size() > threshold();
    }
}
