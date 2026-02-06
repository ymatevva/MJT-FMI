package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.listeners.MockitoListener;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TestCompositeSimilarityCalculator {

    private CompositeSimilarityCalculator compositeSimilarityCalculator;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setup() {
        Map<SimilarityCalculator, Double> similarityCalculatorDoubleMap = new HashMap<>();
        GenresOverlapSimilarityCalculator genresOverlapSimilarityCalculator = Mockito.mock(
            GenresOverlapSimilarityCalculator.class);
        TFIDFSimilarityCalculator tfidfSimilarityCalculator = Mockito.mock(TFIDFSimilarityCalculator.class);

        similarityCalculatorDoubleMap.put(genresOverlapSimilarityCalculator, 0.5);
        similarityCalculatorDoubleMap.put(tfidfSimilarityCalculator, 0.3);

         book1 = Mockito.mock(Book.class);
         book2 = Mockito.mock(Book.class);

        when(genresOverlapSimilarityCalculator.calculateSimilarity(book1, book2)).thenReturn(0.2);
        when(tfidfSimilarityCalculator.calculateSimilarity(book1, book2)).thenReturn(0.1);

        compositeSimilarityCalculator = new CompositeSimilarityCalculator(similarityCalculatorDoubleMap);
    }

    @Test
    void testCalculateSimilarityWhenBookNull() {
        assertThrows(IllegalArgumentException.class, () -> compositeSimilarityCalculator.calculateSimilarity(null, Mockito.mock(
            Book.class)), "Method should throw when one or both books are null");
        assertThrows(IllegalArgumentException.class, () -> compositeSimilarityCalculator.calculateSimilarity(null,
            null), "Method should throw when one or both books are null");
        assertThrows(IllegalArgumentException.class, () -> compositeSimilarityCalculator.calculateSimilarity( Mockito.mock(
            Book.class), null), "Method should throw when one or both books are null");
    }

    @Test
    void testCalculateSimilarity() {
        assertEquals(0.13, compositeSimilarityCalculator.calculateSimilarity(book1, book2), "Method should correctly calculate similarity between two books.");
    }
}
