package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBookFinder {

    private BookFinder bookFinder;
    private TextTokenizer textTokenizer;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;
    private Book book5;
    private Book book6;
    private Book book7;
    private Book book8;
    private Book book9;

    @BeforeEach
    void setup() {
        textTokenizer = mock(TextTokenizer.class);
        book1 = mock(Book.class);
        when(book1.title()).thenReturn("A Court of Thorns and Roses");
        when(book1.author()).thenReturn("Sarah J. Maas");
        when(book1.description()).thenReturn(
            "Feyre is dragged into Prythian and discovers magic, danger, and romance.");
        when(book1.genres()).thenReturn(List.of("Romance", "Fantasy"));
        when(book1.rating()).thenReturn(4.16);
        when(book1.ratingCount()).thenReturn(3929673);
        when(book1.URL()).thenReturn("https://Book_A_Court_of_Thorns_and_Roses");

        book2 = mock(Book.class);
        when(book2.title()).thenReturn("A Court of Mist and Fury");
        when(book2.author()).thenReturn("Sarah J. Maas");
        when(book2.description()).thenReturn(
            "Feyre navigates trauma and power as she discovers the Night Court and Rhysand.");
        when(book2.genres()).thenReturn(List.of("Romance", "Fantasy"));
        when(book2.rating()).thenReturn(4.64);
        when(book2.ratingCount()).thenReturn(2993300);
        when(book2.URL()).thenReturn("https://Book_A_Court_of_Mist_and_Fury");

        book3 = mock(Book.class);
        when(book3.title()).thenReturn("A Court of Wings and Ruin");
        when(book3.author()).thenReturn("Sarah J. Maas");
        when(book3.description()).thenReturn(
            "Feyre returns to the Spring Court and must rally allies for a devastating war.");
        when(book3.genres()).thenReturn(List.of("Romance", "Fantasy", "Action"));
        when(book3.rating()).thenReturn(4.47);
        when(book3.ratingCount()).thenReturn(2520562);
        when(book3.URL()).thenReturn("https://Book_A_Court_of_Wings_and_Ruin");

        book4 = mock(Book.class);
        when(book4.title()).thenReturn("A Court of Silver Flames");
        when(book4.author()).thenReturn("Sarah J. Maas");
        when(book4.description()).thenReturn("Nesta must heal and train with Cassian while war looms over the courts.");
        when(book4.genres()).thenReturn(List.of("Romance", "Fantasy", "New Adult"));
        when(book4.rating()).thenReturn(4.45);
        when(book4.ratingCount()).thenReturn(1924743);
        when(book4.URL()).thenReturn("https://Book_A_Court_of_Silver_Flames");

        book5 = mock(Book.class);
        when(book5.title()).thenReturn("Throne of Glass");
        when(book5.author()).thenReturn("Sarah J. Maas");
        when(book5.description()).thenReturn(
            "A skilled assassin is offered freedom only if she wins a deadly competition.");
        when(book5.genres()).thenReturn(List.of("Fantasy", "Adventure"));
        when(book5.rating()).thenReturn(4.18);
        when(book5.ratingCount()).thenReturn(2225044);
        when(book5.URL()).thenReturn("https://Book_Throne_of_Glass");

        book6 = mock(Book.class);
        when(book6.title()).thenReturn("Heir of Fire");
        when(book6.author()).thenReturn("Sarah J. Maas");
        when(book6.description()).thenReturn(
            "Aelin travels to a foreign land to reclaim her throne amid new alliances.");
        when(book6.genres()).thenReturn(List.of("Fantasy", "Adventure", "Coming-of-Age"));
        when(book6.rating()).thenReturn(4.45);
        when(book6.ratingCount()).thenReturn(1431282);
        when(book6.URL()).thenReturn("https://Book_Heir_of_Fire");

        book7 = mock(Book.class);
        when(book7.title()).thenReturn("House of Earth and Blood");
        when(book7.author()).thenReturn("Sarah J. Maas");
        when(book7.description()).thenReturn(
            "In Crescent City, Bryce investigates her best friend’s murder with a brooding warrior.");
        when(book7.genres()).thenReturn(List.of("Urban Fantasy", "Romance", "Mystery"));
        when(book7.rating()).thenReturn(4.39);
        when(book7.ratingCount()).thenReturn(2406088);
        when(book7.URL()).thenReturn("https://Book_House_of_Earth_and_Blood");

        book8 = mock(Book.class);
        when(book8.title()).thenReturn("Murder on the Orient Express");
        when(book8.author()).thenReturn("Agatha Christie");
        when(book8.description()).thenReturn(
            "Detective Hercule Poirot investigates a murder aboard a luxurious train.");
        when(book8.genres()).thenReturn(List.of("Mystery", "Crime", "Classic"));
        when(book8.rating()).thenReturn(4.2);
        when(book8.ratingCount()).thenReturn(1094300);
        when(book8.URL()).thenReturn("https://Book_Murder_on_the_Orient_Express");

        book9 = mock(Book.class);
        when(book9.title()).thenReturn("The Great Gatsby");
        when(book9.author()).thenReturn("F. Scott Fitzgerald");
        when(book9.description()).thenReturn(
            "Nick Carraway narrates the tragic story of Jay Gatsby and his pursuit of the American Dream.");
        when(book9.genres()).thenReturn(List.of("Classic", "Literature", "Romance"));
        when(book9.rating()).thenReturn(3.92);
        when(book9.ratingCount()).thenReturn(4978300);
        when(book9.URL()).thenReturn("https://Book_The_Great_Gatsby");
        bookFinder =
            new BookFinder(Set.of(book1, book2, book3, book4, book5, book6, book7, book8, book9), textTokenizer);
    }

    @Test
    void testAllGenres() {
        Set<String> allGenres =
            Set.of("Romance", "Fantasy", "Action", "New Adult", "Urban Fantasy", "Mystery", "Adventure",
                "Coming-of-Age", "Crime", "Classic", "Literature");

        assertEquals(allGenres, bookFinder.allGenres(),
            "Method should return a set of all genres of the books in the database.");
        assertNotEquals(Set.of("Romance"), bookFinder.allGenres(),
            "Method should return a set of all genres of the books in the database.");
    }

    @Test
    void testAllBooksByAuthor() {
        assertEquals(List.of(book8), bookFinder.searchByAuthor("Agatha Christie"),
            "Method should return all books by author.");
        assertEquals(List.of(), bookFinder.searchByAuthor("Agatha Kristie"),
            "Method should return empty set when author is not recognized in the book database.");
    }

    @Test
    void testSearchByAllGenres() {
        assertTrue(List.of(book1, book2, book3, book4)
            .containsAll(bookFinder.searchByGenres(Set.of("Fantasy", "Romance"), MatchOption.MATCH_ALL))
        && bookFinder.searchByGenres(Set.of("Fantasy", "Romance"), MatchOption.MATCH_ALL).size() == 4,
            "Method should return all books which contain all the given genres.");
    }
}
