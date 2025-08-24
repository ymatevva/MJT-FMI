    package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

    import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
    import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.Mock;
    import org.mockito.Mockito;

    import java.util.Collection;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Map;
    import java.util.Set;
    import java.util.stream.Collectors;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;

    // NOT COMPLETED YET
    public class TestTFIDFSimilarityCalculator {

        private TFIDFSimilarityCalculator tfidfSimilarityCalculator;
        private TextTokenizer textTokenizer;
        private Book book1;
        private Book book2;
        private Book book3;

        @BeforeEach
        void setup() {
            book1 = mock(Book.class);
            when(book1.title()).thenReturn("A Court of Thorns and Roses");
            when(book1.description()).thenReturn(
                "Feyre, a young mortal huntress, is taken to the magical land of Prythian after killing a wolf in the woods. " +
                    "She meets powerful Fae, faces dangers at every turn, and slowly learns about love, trust, and sacrifice. " +
                    "As she explores this new world, she must make difficult choices to survive and protect those she cares about.");

            book2 = mock(Book.class);
            when(book2.title()).thenReturn("A Court of Mist and Fury");
            when(book2.description()).thenReturn(
                "After the events of the first book, Feyre struggles with her past and tries to find her place in the Night Court. " +
                    "She discovers new powers, meets new friends, and faces challenges that test her courage and heart. " +
                    "Through it all, she learns what it really means to love, forgive, and fight for what is right.");

            book3 = mock(Book.class);
            when(book3.title()).thenReturn("A Court of Wings and Ruin");
            when(book3.description()).thenReturn(
                "Feyre returns to the Spring Court to uncover secrets and prepare for a dangerous war. " +
                    "She faces enemies, makes alliances, and fights to protect her family and friends. " +
                    "With each choice, she learns that bravery, loyalty, and hope can make all the difference in a world full of danger.");

            textTokenizer = Mockito.mock(TextTokenizer.class);

            List<String> tokensBookOne =  List.of(
                "feyre", "young", "mortal", "huntress", "taken", "magical", "land", "prythian", "killing", "wolf", "woods",
                "meets", "powerful", "fae", "faces", "dangers", "every", "turn", "slowly", "learns", "love", "trust", "sacrifice",
                "explores", "new", "world", "must", "make", "difficult", "choices", "survive", "protect", "cares"
            );

            List<String> tokensBookTwo =  List.of(
                "events", "first", "book", "feyre", "struggles", "past", "tries", "find", "place", "night", "court",
                "discovers", "new", "powers", "meets", "new", "friends", "faces", "challenges", "test", "courage", "heart",
                "learns", "really", "means", "love", "forgive", "fight", "right"
            );

            List<String> tokensBookThree =  List.of(
                "feyre", "returns", "spring", "court", "uncover", "secrets", "prepare", "dangerous", "war",
                "faces", "enemies", "makes", "alliances", "fights", "protect", "family", "friends",
                "choice", "learns", "bravery", "loyalty", "hope", "make", "difference", "world", "full", "danger"
            );

            when(textTokenizer.tokenize(book1.description())).thenReturn(tokensBookOne);
            when(textTokenizer.tokenize(book2.description())).thenReturn(tokensBookTwo);
            when(textTokenizer.tokenize(book3.description())).thenReturn(tokensBookThree);

            tfidfSimilarityCalculator = new TFIDFSimilarityCalculator(Set.of(book1, book2, book3), textTokenizer);
        }

        @Test
        void testSimilarityWhenBookNull() {
            assertThrows(IllegalArgumentException.class, () -> tfidfSimilarityCalculator.calculateSimilarity(null, Mockito.mock(Book.class)), "Method should throw when one or both books are null");
            assertThrows(IllegalArgumentException.class, () -> tfidfSimilarityCalculator.calculateSimilarity(null, null), "Method should throw when one or both books are null");
            assertThrows(IllegalArgumentException.class, () -> tfidfSimilarityCalculator.calculateSimilarity(Mockito.mock(Book.class), null), "Method should throw when one or both books are null");
        }

        private Map<String, Double> calculateTF(Book book) {
            Map<String, Long> counts = textTokenizer.tokenize(book.description()).stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            Map<String, Double> bookTF = new HashMap<>();
            for (var e : counts.entrySet()) {
                bookTF.put(e.getKey(), e.getValue() / (double) textTokenizer.tokenize(book.description()).size());
            }

            return bookTF;
        }

        private Map<String, Double> caclulateIDF(Book book) {
            List<List<String>> allBooks = List.of(textTokenizer.tokenize(book1.description()),
                textTokenizer.tokenize(book2.description()), textTokenizer.tokenize(book3.description()));

            Map<String, Double> bookIDF = new HashMap<>();

            for (String word : textTokenizer.tokenize(book.description())) {
                long df = allBooks.stream()
                    .filter(bookTokens -> bookTokens.contains(word))
                    .count();

                double idf = Math.log((double) allBooks.size() / (df + 1));
                bookIDF.putIfAbsent(word, idf);
            }
            return bookIDF;
        }

        private Map<String, Double> computeTFIDF(Book book) {

            Map<String, Double> bookTF = calculateTF(book);
            Map<String, Double> bookIDF = caclulateIDF(book);

            Map<String, Double> bookTFIDF = new HashMap<>(bookTF);

            for(var word : bookTFIDF.entrySet()) {
                bookTFIDF.put(word.getKey(), bookTFIDF.get(word.getKey()) * bookIDF.get(word.getKey()));
            }

            return bookTFIDF;
        }

        private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
            double magnitudeFirst = magnitude(first.values());
            double magnitudeSecond = magnitude(second.values());

            return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
        }

        private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
            Set<String> commonKeys = new HashSet<>(first.keySet());
            commonKeys.retainAll(second.keySet());

            return commonKeys.stream()
                .mapToDouble(word -> first.get(word) * second.get(word))
                .sum();
        }

        private double magnitude(Collection<Double> input) {
            double squaredMagnitude = input.stream()
                .map(v -> v * v)
                .reduce(0.0, Double::sum);

            return Math.sqrt(squaredMagnitude);
        }

        @Test
        void testSimilarityBetweenBooks() {
            double cosineBookOneTwo = cosineSimilarity(computeTFIDF(book1), computeTFIDF(book2));
            double cosineBookTwoThree = cosineSimilarity(computeTFIDF(book2), computeTFIDF(book3));
            double cosineBookOneThree = cosineSimilarity(computeTFIDF(book1), computeTFIDF(book3));

            assertEquals(cosineBookOneTwo, tfidfSimilarityCalculator.calculateSimilarity(book1, book2), 0.000000001, "Method should properly calculate the similarity between two books.");
            assertEquals(cosineBookTwoThree, tfidfSimilarityCalculator.calculateSimilarity(book2, book3), 0.000000001,"Method should properly calculate the similarity between two books.");
            assertEquals(cosineBookOneThree, tfidfSimilarityCalculator.calculateSimilarity(book1, book3), 0.00000001, "Method should properly calculate the similarity between two books.");
        }
    }
