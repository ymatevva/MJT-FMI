package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestGenresOverlapSimilarityCalculator {

    private GenresOverlapSimilarityCalculator genresOverlapSimilarityCalculator;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setup() {
        genresOverlapSimilarityCalculator = new GenresOverlapSimilarityCalculator();

        book1 = mock(Book.class);
        when(book1.title()).thenReturn("The Midnight Library");
        when(book1.genres()).thenReturn(List.of("Fiction", "Fantasy", "Philosophical"));

        book2 = mock(Book.class);
        when(book2.title()).thenReturn("The Exceptional");
        when(book2.genres()).thenReturn(List.of("Non-Fiction", "Psychology", "Self-Improvement"));

    }

    @Test
    void testWithNullBooks() {
        assertThrows(IllegalArgumentException.class,
            () -> genresOverlapSimilarityCalculator.calculateSimilarity(mock(Book.class), null),
            "Method should throw when one or both of the books is null.");
    }

    @Test
    void testWhenNoGenresInIntersection() {
        assertEquals(0, genresOverlapSimilarityCalculator.calculateSimilarity(book1, book2),
            "Method should return zero similarity between books which do not have common genres.");
    }

    @Test
    void testWhenBookHasZeroGenres() {

        Book book1 = mock(Book.class);
        when(book1.title()).thenReturn("The Midnight Library");
        when(book1.genres()).thenReturn(List.of("Fiction", "Fantasy", "Philosophical"));

        assertEquals(0, genresOverlapSimilarityCalculator.calculateSimilarity(book1, Mockito.mock(Book.class)),
            "Method should return zero similarity when one or both of the books has zero genres.");
    }

    @Test
    void testWhenBooksHaveCommonGenres() {
        when(book1.genres()).thenReturn(List.of("Romance", "Fiction", "Magic"));
        when(book2.genres()).thenReturn(List.of("Romance", "Drama"));

        assertEquals(0.5, genresOverlapSimilarityCalculator.calculateSimilarity(book1, book2),
            "Method should return correct similarity when there are common genres.");
    }
}
