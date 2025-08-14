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

public class TestLocationRule {

    private LocationsRule locationsRule;
    private List<Transaction> transactions;

    @BeforeEach
    void setup() {
        transactions = new ArrayList<>();
        Transaction transaction1 = Mockito.mock();
        when(transaction1.location()).thenReturn("SOZOPOL");
        Transaction transaction2 = Mockito.mock();
        when(transaction2.location()).thenReturn("SOZOPOL");
        Transaction transaction3 = Mockito.mock();
        when(transaction3.location()).thenReturn("SOZOPOL");
        Transaction transaction4 = Mockito.mock();
        when(transaction4.location()).thenReturn("SUNNY BEACH");
        Transaction transaction5 = Mockito.mock();
        when(transaction5.location()).thenReturn("VARNA");

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
    }

    @Test
    void testApplicableTrueCondition() {
        locationsRule = new LocationsRule(2, 0.2);
        assertTrue(locationsRule.applicable(transactions), "Rule should be applied with more than threshold number of transactions with same location.");
    }

    @Test
    void testApplicableFalseCondition() {
        locationsRule = new LocationsRule(3, 0.2);
        assertFalse(locationsRule.applicable(transactions), "Rule should not be applied with less than threshold number of transactions with same location.");
    }

    @Test
    void testApplicableWithNull() {
        locationsRule = new LocationsRule(2, 1.0);
        assertThrows(IllegalArgumentException.class, () -> locationsRule.applicable(null), "Method should throw when list of transactions is null.");
    }

}
