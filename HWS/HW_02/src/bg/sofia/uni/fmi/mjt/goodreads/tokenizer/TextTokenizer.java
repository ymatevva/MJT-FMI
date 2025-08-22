package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopwords;
    private static final String PUNCTUAL_REGEX = "\\p{Punct}";
    private static final String SPACE_REGEX = "\\s+";

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("The input cannot be null.");
        }

        input = input.replaceAll(PUNCTUAL_REGEX, "");
        input = input.replaceAll(SPACE_REGEX, " ").trim();

        List<String> words = new ArrayList<>(Arrays.stream(input.toLowerCase().split(" ")).toList());

        return words.stream()
            .filter(word -> !stopwords.contains(word))
            .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

}