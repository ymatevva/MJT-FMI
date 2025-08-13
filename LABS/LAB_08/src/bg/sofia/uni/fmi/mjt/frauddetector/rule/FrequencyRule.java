package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule extends RuleImpl {

    private TemporalAmount timeWindow;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
          super(weight, transactionCountThreshold);
          this.timeWindow = timeWindow;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {

        int countOp = transactions.stream()
            .filter(p -> p.transactionDate().range(timeWindow))
    }

}
