package bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TextTokenizer {

    private final Set<String> stopwords;
    private static final String PUNCTUAL_REGEX = "\\p{Punct}";
    private static final String MORE_THAN_ONE_SPACE_REGEX = "\\s+";
    private static final String SPACE_REGEX = " ";
    private static final String NO_SPACE_REGEX = "";

    public TextTokenizer(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<String> tokenize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("The input cannot be null.");
        }

        input = input.replaceAll(PUNCTUAL_REGEX, NO_SPACE_REGEX);
        input = input.replaceAll(MORE_THAN_ONE_SPACE_REGEX, SPACE_REGEX).trim();

        List<String> words = new ArrayList<>(Arrays.stream(input.toLowerCase().split(SPACE_REGEX)).toList());

        return words.stream()
            .filter(word -> !stopwords.contains(word))
            .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

}