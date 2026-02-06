package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestTokenizer {

    private TextTokenizer textTokenizer;
    private static final String PUNCTUAL_REGEX = "\\p{Punct}";
    private static final String SPACE_REGEX = "\\s+";
    private String input;

    @BeforeEach
    void setup() {
        String stopwords = "stopwords.txt";
        try (Reader readerStopwords = new FileReader(stopwords)) {
            textTokenizer = new TextTokenizer(readerStopwords);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        input = "Feyre has returned to the Spring Court, determined to gather information on " +
            "Tamlins actions and learn what she can about the invading king threatening to bring her" +
            " land to its knees. But to do so she must play a deadly game of deceit. One slip could bring " +
            "doom not only for Feyre, but for everything and everyone she holds dear.\n" +
            "\n" +
            "As war bears down upon them all, Feyre endeavors to take her place amongst the " +
            "High Fae of the land, balancing her struggle to master her powers both magical and " +
            "political and her love for her court and family. Amidst. these struggles, Feyre and Rhysand" +
            " must decide whom to trust amongst the cunning and lethal High Lords, and hunt for allies in unexpected places!";
    }

    @Test
    void testTokenizeWithNull() {
        assertThrows(IllegalArgumentException.class, () -> textTokenizer.tokenize(null),
            "Method should throw if input is null.");
    }

    @Test
    void testTokenizeWithDescriptionInput() {
        List<String> tokenized = List.of(
            "feyre", "returned", "spring", "court", "determined", "gather", "information", "tamlins", "actions",
            "learn", "can", "invading", "king", "threatening", "bring", "land", "knees", "must", "play",
            "deadly", "game", "deceit", "one", "slip", "bring", "doom", "feyre", "everything", "everyone",
            "holds", "dear", "war", "bears", "upon", "feyre", "endeavors", "take", "place", "amongst", "high",
            "fae", "land", "balancing", "struggle", "master", "powers", "magical", "political", "love", "court",
            "family", "amidst", "struggles", "feyre", "rhysand", "must", "decide", "trust", "amongst", "cunning",
            "lethal", "high", "lords", "hunt", "allies", "unexpected", "places"
        );

        assertEquals(tokenized, textTokenizer.tokenize(input), "Method should properly turn the input into list of tokens.");
    }

    @Test
    void testGettingStopwords() {
        Set<String> stopwords = Set.of("a",
            "about","above","after","again","against","all","am","an","and","any","are","arent","as","at",
            "be","because","been","before","being","below","between","both","but","by","cant","cannot","could",
            "couldnt","did","didnt","do","does","doesnt","doing","dont","down","during","each","few","for","from",
            "further","had","hadnt","has","hasnt","have","havent","having","he","hed","hell","hes","her","here",
            "heres","hers","herself","him","himself","his","how","hows","i","id","ill","im","ive","if","in","into",
            "is","isnt","it","its","itself","lets","me","more","most","mustnt","my","myself","no","nor","not","of",
            "off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shant",
            "she","shed","shell","shes","should","shouldnt","so","some","such","than","that","thats","the","their",
            "theirs","them","themselves","then","there","theres","these","they","theyd","theyll","theyre","theyve",
            "this","those","through","to","too","under","until","up","very","was","wasnt","we","wed","well","were",
            "weve","werent","what","whats","when","whens","where","wheres","which","while","who","whos","whom",
            "why","whys","with","wont","would","wouldnt","you","youd","youll","youre","youve","your","yours",
            "yourself","yourselves"
        );

        assertEquals(stopwords, textTokenizer.stopwords(), "Method should return a set of the stopwords.");
    }
}
