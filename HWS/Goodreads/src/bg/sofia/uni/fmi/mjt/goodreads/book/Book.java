package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {

    private static final int ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int GENRES_INDEX = 4;
    private static final int RATING_INDEX = 5;
    private static final int RATING_COUNT_INDEX = 6;
    private static final int URL_INDEX = 7;
    private static final int TOKENS_SIZE = 8;
    private static final String SPLIT_SYMBOL = ",";
    private static final String GENRE_REGEX = "[\\[\\]\n]";

    public static Book of(String[] tokens) {
        if (tokens.length != TOKENS_SIZE) {
            throw new IllegalArgumentException("Tokens length should be correct.");
        }

        if (Arrays.stream(tokens).filter(token -> token == null).count() != 0) {
            throw new IllegalArgumentException("Data for tokens cannot be null");
        }

        List<String> genres = Arrays.stream(tokens[GENRES_INDEX]
                .replaceAll(GENRE_REGEX, "")
                .split(SPLIT_SYMBOL))
            .map(String::trim)
            .toList();

        return new Book(tokens[ID_INDEX], tokens[TITLE_INDEX], tokens[AUTHOR_INDEX],
            tokens[DESCRIPTION_INDEX], genres,
            Double.parseDouble(tokens[RATING_INDEX]),
            Integer.parseInt(tokens[RATING_COUNT_INDEX].replaceAll(",", "")), tokens[URL_INDEX]);
    }

}