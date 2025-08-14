package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestZScoreRule {

    private ZScoreRule zScoreRule;
    private List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();
        Transaction transaction1 = Mockito.mock(Transaction.class);
        when(transaction1.transactionAmount()).thenReturn(2.0);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        when(transaction2.transactionAmount()).thenReturn(1.0);
        Transaction transaction3 = Mockito.mock(Transaction.class);
        when(transaction3.transactionAmount()).thenReturn(3.0);
        Transaction transaction4 = Mockito.mock(Transaction.class);
        when(transaction4.transactionAmount()).thenReturn(3.5);
        Transaction transaction5 = Mockito.mock(Transaction.class);
        when(transaction5.transactionAmount()).thenReturn(3.7);
        Transaction transaction6 = Mockito.mock(Transaction.class);
        when(transaction6.transactionAmount()).thenReturn(8.0);
        Transaction transaction7 = Mockito.mock(Transaction.class);
        when(transaction7.transactionAmount()).thenReturn(4.0);

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
        transactions.add(transaction7);
    }

    @Test
    void testApplicableTrueCondition() {
        zScoreRule = new ZScoreRule(1.5, 0.5);
        assertTrue(zScoreRule.applicable(transactions), "Rule should be applied when for any transaction z score is bigger than the threshold");
    }

    @Test
    void testApplicableFalseCondition() {
        zScoreRule = new ZScoreRule(2.16, 0.5);
        assertFalse(zScoreRule.applicable(transactions), "Rule should not be applied when there are no transactions where z score is bigger than the threshold");
    }

    @Test
    void testApplicableWithNull() {
        zScoreRule = new ZScoreRule(2.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> zScoreRule.applicable(null), "Method should throw when list of transactions is null.");
    }

    @Test
    void testApplicableWithZeroVariance() {
        zScoreRule = new ZScoreRule(2.0, 1.0);
        Transaction transaction1 = Mockito.mock(Transaction.class);
        when(transaction1.transactionAmount()).thenReturn(1.0);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        when(transaction2.transactionAmount()).thenReturn(1.0);
        assertThrows(UnsupportedOperationException.class, () -> zScoreRule.applicable(List.of(transaction1, transaction2)), "Method should throw when variance is zero.");
    }
}
