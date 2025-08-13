package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestFrequencyRule {

    private FrequencyRule frequencyRule;
    private List<Transaction> transactions;

    @BeforeEach
    void setup() {
         transactions = new ArrayList<>();

        Transaction transaction1 = Mockito.mock(Transaction.class);
        when(transaction1.transactionDate()).thenReturn(LocalDateTime.parse("2025-08-09T00:00"));

        Transaction transaction2 = Mockito.mock(Transaction.class);
        when(transaction2.transactionDate()).thenReturn(LocalDateTime.parse("2025-08-15T00:00"));

        Transaction transaction3 = Mockito.mock(Transaction.class);
        when(transaction3.transactionDate()).thenReturn(LocalDateTime.parse("2025-08-20T00:00"));

        Transaction transaction4 = Mockito.mock(Transaction.class);
        when(transaction4.transactionDate()).thenReturn(LocalDateTime.parse("2025-08-01T00:00"));

        Transaction transaction5 = Mockito.mock(Transaction.class);
        when(transaction5.transactionDate()).thenReturn(LocalDateTime.parse("2025-08-10T00:00"));

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
    }
        @Test
    void testApplicableToBeTrue() {
        frequencyRule = new FrequencyRule(4, Period.ofDays(20), 0.7);
        assertTrue(frequencyRule.applicable(transactions), "Rule should be applied with at least threshold transactions in given period of time.");
    }

    @Test
    void testApplicableToBeFalse() {
        frequencyRule = new FrequencyRule(2, Period.ofDays(3), 0.2);
        assertFalse(frequencyRule.applicable(transactions), "Rule should not be applied with less than threshold transactions in given period of time.");
    }
}
