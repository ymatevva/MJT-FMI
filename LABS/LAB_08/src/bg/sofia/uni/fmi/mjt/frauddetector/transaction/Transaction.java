package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    // factory method
    public static Transaction of(String line) {

        // splitting a line of the .csv

      // TO DO: not the best practice to have magic numbers
      
        String[] data = line.split(",");

        Channel transactionChannel = switch (data[5].toLowerCase()) {
            case "atm" -> Channel.ATM;
            case "branch" -> Channel.BRANCH;
            case "online" -> Channel.ONLINE;
            default -> throw new IllegalArgumentException("The channel is unknown.");
        };

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(data[3], pattern);

        return new Transaction(data[0], data[1], Double.parseDouble(data[2]), dateTime, data[4], transactionChannel);
    }

    }
