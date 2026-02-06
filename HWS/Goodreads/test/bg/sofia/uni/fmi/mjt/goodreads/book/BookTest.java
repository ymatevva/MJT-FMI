package bg.sofia.uni.fmi.mjt.goodreads.book;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testWithValidData() {
        String[] tokens = {"ID1", "TITLE1", "AUTHOR1", "DESC1", "[Romance, Magic]", "100", "1,234,222", "URL1"};

        Book book = Book.of(tokens);

        assertEquals( tokens[0], book.ID(), "Method should return correct ID.");
        assertEquals( tokens[2], book.author(),"Method should return correct author.");
        assertEquals(tokens[1],book.title(),  "Method should return correct title.");
        assertEquals(tokens[3],book.description(),  "Method should return correct description.");
        assertEquals(List.of("Romance", "Magic"),book.genres(),  "Method should return correct genres.");
        assertEquals(100, book.rating(), "Method should return correct rating.");
        assertEquals(1234222,book.ratingCount(),  "Method should return correct rating count.");
        assertEquals(tokens[7], book.URL(), "Method should return correct URL.");
    }

    @Test
    void testWithInvalidNumberTokens() {
        String[] tokens = {"ID1", "TITLE1", "AUTHOR1", "[Romance, Magic]", "100", "1,234,222", "URL1"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens), "Factory method should throw with invalid number of tokens.");
    }

    @Test
    void testWithInvalidData() {
        String[] tokens = {null, "TITLE1", "AUTHOR1", "Desc", "[Romance, Magic]", "100", "1,234,222", "URL1"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens), "Factory method should throw with invalid data for token.");
    }


}