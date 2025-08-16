package bg.sofia.uni.fmi.mjt.football;

import javax.swing.JToolBar;
import java.nio.file.LinkOption;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    private static final String SEPARATOR = ";";
    private static final int NAME_IND = 0;
    private static final int FULL_NAME_IND = 1;
    private static final int BIRTH_DATE_IND = 2;
    private static final int AGE_IND = 3;
    private static final int HEIGHT_IND = 4;
    private static final int WEIGHT_IND = 5;
    private static final int POSITIONS_IND = 6;
    private static final int NATIONALITY_IND = 7;
    private static final int RATING_IND = 8;
    private static final int POTENTIAL_IND = 9;
    private static final int VALUE_EURO_IND = 10;
    private static final int WAGE_EURO_IND = 11;
    private static final int PREFERRED_FOOT_IND = 12;

    public static Player of(String line) {

        String[] data = line.split(SEPARATOR);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        List<Position> positions = Arrays.stream(data[POSITIONS_IND].split(","))
            .map(Position::of)
            .toList();

        return new Player(data[NAME_IND], data[FULL_NAME_IND], LocalDate.parse(data[BIRTH_DATE_IND], formatter),
            Integer.parseInt(data[AGE_IND]),
            Double.parseDouble(data[HEIGHT_IND]), Double.parseDouble(data[WEIGHT_IND]), positions,
            data[NATIONALITY_IND], Integer.parseInt(data[RATING_IND]),
            Integer.parseInt(data[POTENTIAL_IND]), Long.parseLong(data[VALUE_EURO_IND]),
            Long.parseLong(data[WAGE_EURO_IND]), Foot.of(data[PREFERRED_FOOT_IND]));
    }
}
