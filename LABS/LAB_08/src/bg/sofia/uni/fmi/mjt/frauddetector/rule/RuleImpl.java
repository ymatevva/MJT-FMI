package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class RuleImpl implements  Rule {

    // this class is made in order not to repeat the getWeight() and getThreshold() in each rule

    private double weight;
    private double threshold;

    public RuleImpl(double weight, double threshold) {
        this.weight = weight;
        this.threshold = threshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        return false;
    }

    @Override
    public double weight() {
        return weight;
    }

    public double threshold() {
        return threshold;
    }
}
