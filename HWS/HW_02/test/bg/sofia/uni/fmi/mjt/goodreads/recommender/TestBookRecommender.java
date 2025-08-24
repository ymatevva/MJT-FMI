package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TestBookRecommender {

    private BookRecommender bookRecommender;
    private SimilarityCalculator calculator;

    private Book book1;
    private Book book2;
    private Book book3;
    private Set<Book> books;

    @BeforeEach
    void setup() {
        book1 = Mockito.mock(Book.class);
        book2 = Mockito.mock(Book.class);
        book3 = Mockito.mock(Book.class);

        books = Set.of(book1, book2, book3);
    }

    @Test
    void testWhenMaxNIsNotPositive() {
        bookRecommender = new BookRecommender(Set.of(), Mockito.mock(SimilarityCalculator.class));
        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(Mockito.mock(Book.class), 0),
            "Method should throw when maxN is zero.");
        assertThrows(IllegalArgumentException.class,
            () -> bookRecommender.recommendBooks(Mockito.mock(Book.class), -10),
            "Method should throw when maxN is negative.");
    }

    @Test
    void testWhenBookIsNull() {
        bookRecommender = new BookRecommender(Set.of(), Mockito.mock(SimilarityCalculator.class));
        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(null, 10),
            "Method should throw when book is null.");
    }

    @Test
    void testBookRecommender() {
        calculator = Mockito.mock(GenresOverlapSimilarityCalculator.class);

        bookRecommender = new BookRecommender(books, calculator);
        Book myBook = Mockito.mock(Book.class);

        when(calculator.calculateSimilarity(book1, myBook)).thenReturn(1.0);
        when(calculator.calculateSimilarity(book2, myBook)).thenReturn(0.3);
        when(calculator.calculateSimilarity(book3, myBook)).thenReturn(0.5);

        assertTrue(bookRecommender.recommendBooks(myBook, 2).containsKey(book1)
        && bookRecommender.recommendBooks(myBook, 2).containsKey(book3)
            && bookRecommender.recommendBooks(myBook,2).size() == 2, "Method should return sorted map of top N recommended books");
    }

}
