package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.lang.invoke.StringConcatFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private static final String SEPARATOR = ",";
    private static final int TRANSACTION_ID_INDEX = 0;
    private static final int ACCOUNT_ID_INDEX = 1;
    private static final int TRANSACTION_AMOUNT_INDEX = 2;
    private static final int TRANSACTION_DATE_INDEX = 3;
    private static final int TRANSACTION_LOCATION_INDEX = 4;
    private static final int TRANSACTION_CHANNEL_INDEX = 5;

    // factory method
    public static Transaction of(String line) {

        // splitting a line of the .csv
        String[] data = line.split(SEPARATOR);

        Channel transactionChannel = switch (data[TRANSACTION_CHANNEL_INDEX].toLowerCase()) {
            case "atm" -> Channel.ATM;
            case "branch" -> Channel.BRANCH;
            case "online" -> Channel.ONLINE;
            default -> throw new IllegalArgumentException("The channel is unknown.");
        };

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(data[TRANSACTION_DATE_INDEX], pattern);

        return new Transaction(data[TRANSACTION_ID_INDEX], data[ACCOUNT_ID_INDEX],
            Double.parseDouble(data[TRANSACTION_AMOUNT_INDEX]), dateTime, data[TRANSACTION_LOCATION_INDEX],
            transactionChannel);
    }
}
