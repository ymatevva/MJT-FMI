package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {

    private Set<Book> books;
    private TextTokenizer textTokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.textTokenizer = tokenizer;
    }

    @Override
    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
            .map(this::getGenres)
            .flatMap(List::stream)
            .collect(Collectors.toSet());

    }

    private List<String> getGenres(Book book) {
        return book.genres().stream().toList();
    }
    // DONE
    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("The author's name is null or is empty.");
        }

        return books.stream()
            .filter(book -> book.author().equals(authorName))
            .toList();
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null) {
            throw new IllegalArgumentException("The genres set cannot be null.");
        }

        switch (option) {
            case MATCH_ANY -> books.stream()
                .filter(book -> containsAnyGenre(book, genres))
                .toList();
            case MATCH_ALL -> books.stream()
                .filter(book -> containsAllGenre(book, genres))
                .toList();
            default -> {
                throw new IllegalArgumentException("The match option is not valid.");
            }
        }
        return List.of();
    }

    /**
     * Searches for books that match the specified keywords.
     * The search can be based on different match options (all or any keywords).
     *
     * @param keywords a {@code Set} of keywords to search for.
     * @param option   the {@code MatchOption} that defines the search criteria
     *                 (either {@link MatchOption#MATCH_ALL} or {@link MatchOption#MATCH_ANY}).
     * @return a List of books in which the title or description match the given keywords according to the MatchOption
     * Returns an empty list if no books are found.
     */
    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        return List.of();
    }

    private boolean containsAnyGenre(Book book, Set<String> genres) {
        return genres.stream()
            .anyMatch(genre -> book.genres().contains(genre));
    }

    private boolean containsAllGenre(Book book, Set<String> genres) {
        return book.genres().containsAll(genres);
    }

}