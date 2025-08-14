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

public class TestSmallTransactionRule {

    private SmallTransactionsRule smallTransactionsRule;
    private List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();
        Transaction transaction1 = Mockito.mock();
        when(transaction1.transactionAmount()).thenReturn(0.8);
        Transaction transaction2 = Mockito.mock();
        when(transaction1.transactionAmount()).thenReturn(0.4);
        Transaction transaction3 = Mockito.mock();
        when(transaction1.transactionAmount()).thenReturn(0.9);
        Transaction transaction4 = Mockito.mock();
        when(transaction1.transactionAmount()).thenReturn(1.2);
        Transaction transaction5 = Mockito.mock();
        when(transaction1.transactionAmount()).thenReturn(1.7);

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
    }
    @Test
    void testApplicableTrueCondition() {
        smallTransactionsRule = new SmallTransactionsRule(3, 1, 0.3);
        assertTrue(smallTransactionsRule.applicable(transactions), "Rule should be applied with more than threshold number of transactions smaller than the given amount.");
    }

    @Test
    void testApplicableFalseCondition() {
        smallTransactionsRule = new SmallTransactionsRule(4, 1, 0.2);
        assertFalse(smallTransactionsRule.applicable(transactions), "Rule should not be applied with less than threshold number of transactions smaller than the given amount.");
    }

    @Test
    void testApplicableWithNull() {
        smallTransactionsRule = new SmallTransactionsRule(3,2.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> smallTransactionsRule.applicable(null), "Method should throw when list of transactions is null.");
    }

}
