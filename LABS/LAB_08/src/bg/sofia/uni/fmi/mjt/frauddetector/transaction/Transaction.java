package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public record Transaction(String transactionID, String accountID, double transactionAmount, LocalDateTime transactionDate, String location, Channel channel) {

    public static Transaction of(String line) {
        
        return new Transaction();
    }
}
