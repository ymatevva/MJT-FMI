package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    public static Transaction of(String line) {

        String[] data = line.split(",");

        Channel transactionChannel = switch (data[5]) {
            case "ATM" -> Channel.ATM;
            case "Branch" -> Channel.BRANCH;
            case "Online" -> Channel.ONLINE;
            default -> throw new IllegalArgumentException("The channel is unknown.");
        };

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(data[3], pattern);

        return new Transaction(data[0], data[1], Double.parseDouble(data[2]), dateTime, data[4], transactionChannel);
    }
}
