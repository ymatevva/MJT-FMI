package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestTransactionAnalyzerImpl {

    private TransactionAnalyzerImpl transactionAnalyzer;
    private Reader bufferedReader;
    private File testFile;

    private LocationsRule locationsRule;
    private FrequencyRule frequencyRule;
    private SmallTransactionsRule smallTransactionsRule;
    private ZScoreRule zScoreRule;

  // TO DO: still in progress
  
    @BeforeEach
    void setup() throws Exception {

        frequencyRule = Mockito.mock(FrequencyRule.class);
        when(frequencyRule.weight()).thenReturn(0.5);
        locationsRule = Mockito.mock(LocationsRule.class);
        when(locationsRule.weight()).thenReturn(0.2);
        zScoreRule = Mockito.mock(ZScoreRule.class);
        when(zScoreRule.weight()).thenReturn(0.2);
        smallTransactionsRule = Mockito.mock(SmallTransactionsRule.class);
        when(smallTransactionsRule.weight()).thenReturn(0.1);

        Transaction transaction1 = Transaction.of("TR1,AC1,5.0,2025-09-08 16:29:14,SZPL,ATM");
        Transaction transaction2 = Transaction.of("TR2,AC1,10.0,2025-11-08 16:30:15,SZPL,ATM");
        Transaction transaction3 = Transaction.of("TR3,AC2,15.0,2025-01-08 16:17:50,BS,Online");
        Transaction transaction4 = Transaction.of("TR4,AC3,100.0,2025-02-08 15:13:23,NESSEBAR,ATM");

        testFile = File.createTempFile("test", ".csv");
        testFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(
                "TransactionID, AccountID, Amount, TimeOfTransaction, Location, BankingType" + System.lineSeparator());
            writer.write("TR1,AC1,5.0,2025-09-08 16:29:14,SZPL,ATM" + System.lineSeparator());
            writer.write("TR2,AC1,10.0,2025-11-08 16:30:15,SZPL,ATM" + System.lineSeparator());
            writer.write("TR3,AC2,15.0,2025-01-08 16:17:50,BS,Online" + System.lineSeparator());
            writer.write("TR4,AC3,100.0,2025-02-08 15:13:23,NESSEBAR,ATM" + System.lineSeparator());
        }

        bufferedReader = new FileReader(testFile);
        transactionAnalyzer = new TransactionAnalyzerImpl(bufferedReader,
            List.of(frequencyRule, locationsRule, smallTransactionsRule, zScoreRule));

    }

    @Test
    void testGetAllAccountsIDs() {
        assertTrue(transactionAnalyzer.allAccountIDs().containsAll(List.of("AC1", "AC2", "AC3")) &&
            transactionAnalyzer.allAccountIDs().size() == 3, "The method should return all unique account IDs.");
    }

    @Test
    void testGetAllAccountsIDsWithZeroAccounts() throws Exception {
        File emptyFile = File.createTempFile("empty", ".csv");
        bufferedReader = new FileReader(emptyFile);
        transactionAnalyzer = new TransactionAnalyzerImpl(bufferedReader,
            List.of(locationsRule, frequencyRule, zScoreRule, smallTransactionsRule));
        assertTrue(transactionAnalyzer.allAccountIDs().size() == 0, "The method should not find any account IDs.");
    }

    @Test
    void testTransactionsByChannel() {
        assertEquals(3, transactionAnalyzer.transactionCountByChannel().get(Channel.ATM),
            "Method should return correct count of transaction type.");
        assertEquals(1, transactionAnalyzer.transactionCountByChannel().get(Channel.ONLINE),
            "Method should return correct count of transaction type.");
        assertEquals(null, transactionAnalyzer.transactionCountByChannel().get(Channel.BRANCH),
            "Method should return correct count of transaction type.");
    }

    @Test
    void testAmountSpentByUser() {
        assertEquals(15.0, transactionAnalyzer.amountSpentByUser("AC1"),
            "Method should return correct amount spent by user.");
        assertEquals(15.0, transactionAnalyzer.amountSpentByUser("AC2"),
            "Method should return correct amount spent by user.");
        assertEquals(100.0, transactionAnalyzer.amountSpentByUser("AC3"),
            "Method should return correct amount spent by user.");
        assertNotEquals(100.1, transactionAnalyzer.amountSpentByUser("AC3"),
            "Method should return correct amount spent by user.");
    }

    @Test
    void testMethodsWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.amountSpentByUser(null),
            "Method should throw when user is null.");
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.allTransactionsByUser(null),
            "Method should throw when user is null.");
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.accountRating(null),
            "Method should throw when user is null.");
    }

    @Test
    void testMethodsWhenUserIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.amountSpentByUser(""),
            "Method should throw when user is null.");
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.allTransactionsByUser(""),
            "Method should throw when user is null.");
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.accountRating(""),
            "Method should throw when user is null.");
    }

    @Test
    void testGetAllTransactions() {

        assertTrue(transactionAnalyzer.allTransactions()
            .containsAll(transactionAnalyzer.allTransactions()) &&
            transactionAnalyzer.allTransactions().size() == 4, "Method should return correctly all transactions.");
    }

    @Test
    void testAllTransactionsByUser() {

        Transaction transaction1 = Transaction.of("TR1,AC1,5.0,2025-09-08 16:29:14,SZPL,ATM");
        Transaction transaction2 = Transaction.of("TR2,AC1,10.0,2025-11-08 16:30:15,SZPL,ATM");
        Transaction transaction3 = Transaction.of("TR3,AC2,15.0,2025-01-08 16:17:50,BS,Online");
        Transaction transaction4 = Transaction.of("TR4,AC3,100.0,2025-02-08 15:13:23,NESSEBAR,ATM");

        assertTrue(transactionAnalyzer.allTransactionsByUser("AC1").containsAll(List.of(transaction1, transaction2)) &&
                transactionAnalyzer.allTransactionsByUser("AC1").size() == 2,
            "Method should return correctly all transactions by user.");

        assertTrue(transactionAnalyzer.allTransactionsByUser("AC2").containsAll(List.of(transaction3)) &&
                transactionAnalyzer.allTransactionsByUser("AC2").size() == 1,
            "Method should return correctly all transactions by user.");

        assertTrue(transactionAnalyzer.allTransactionsByUser("AC3").containsAll(List.of(transaction4)) &&
                transactionAnalyzer.allTransactionsByUser("AC3").size() == 1,
            "Method should return correctly all transactions by user.");
    }

    @Test
    void testAccountRatingByUser() {
        when(locationsRule.applicable(transactionAnalyzer.allTransactionsByUser("AC1"))).thenReturn(true);
        when(frequencyRule.applicable(transactionAnalyzer.allTransactionsByUser("AC1"))).thenReturn(true);

        when(smallTransactionsRule.applicable(transactionAnalyzer.allTransactionsByUser("AC2"))).thenReturn(true);
        when(zScoreRule.applicable(transactionAnalyzer.allTransactionsByUser("AC2"))).thenReturn(true);

        when(frequencyRule.applicable(transactionAnalyzer.allTransactionsByUser("AC3"))).thenReturn(true);

        assertEquals(0.7, transactionAnalyzer.accountRating("AC1"), 0.0001,
            "Method should calculate account rating correctly.");
        assertEquals(0.3, transactionAnalyzer.accountRating("AC2"), 0.0001,
            "Method should calculate account rating correctly.");
        assertEquals(0.5, transactionAnalyzer.accountRating("AC3"), 0.0001,
            "Method should calculate account rating correctly.");
    }

    @Test
    void testWhenRuleWeightExceeds() {
        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(bufferedReader, List.of(new ZScoreRule(0.2, 1.3))),
            "Method should throw when rules total weight exceeds 1.");
    }
}

