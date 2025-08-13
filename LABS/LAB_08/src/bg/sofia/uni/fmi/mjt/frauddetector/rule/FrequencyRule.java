package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FrequencyRule extends RuleImpl {

    private TemporalAmount timeWindow;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        super(weight, transactionCountThreshold);
        this.timeWindow = timeWindow;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {

        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort((t1, t2) -> t1.transactionDate().compareTo(t2.transactionDate()));

        for (int i = 0; i < transactions.size(); i++) {

            LocalDateTime startTime = transactions.get(i).transactionDate();
            LocalDateTime endTime = startTime.plus(timeWindow);

            int countInTimeWindow = (int) transactions.stream() // checks how many operations are made in this tim window
                .filter(t -> !t.transactionDate().isBefore(startTime) && !t.transactionDate().isAfter(endTime))
                .count();

            // if more than super.threshold() operations are made then the rule can be applied
            if (countInTimeWindow > super.threshold()) {
                return true;
            }
        }
        return false;
    }

}
