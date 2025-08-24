package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.List;
import java.util.Map;
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

        if (!option.equals(MatchOption.MATCH_ALL) && !option.equals(MatchOption.MATCH_ANY)) {
            throw new IllegalArgumentException("The match option is not valid.");
        }

        switch (option) {
            case MATCH_ANY -> {
                return books.stream()
                    .filter(book -> containsAnyGenre(book, genres))
                    .toList();
            }
            case MATCH_ALL -> {
                return books.stream()
                    .filter(book -> containsAllGenre(book, genres))
                    .toList();
            }
            default -> {
                throw new IllegalArgumentException("The match option is not valid.");
            }
        }
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {

        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("The keywords cannot be null or empty.");
        }

        if (!option.equals(MatchOption.MATCH_ALL) && !option.equals(MatchOption.MATCH_ANY)) {
            throw new IllegalArgumentException("The match option is not valid.");
        }

        return books.stream()
            .filter( book -> switch(option) {
                case MATCH_ANY -> keywordIn(keywords, book);
                case MATCH_ALL -> allKeywordsIn(keywords, book);
            })
            .toList();
    }

    private boolean keywordIn(Set<String> keywords, Book book) {
        return keywords.stream()
            .anyMatch(keyword -> book.description().contains(keyword)
                                     || book.title().contains(keyword));
    }

    private boolean allKeywordsIn(Set<String> keywords, Book book) {
        return keywords.stream()
            .allMatch(keyword -> book.description().contains(keyword)
                || book.title().contains(keyword));
    }

    private boolean containsAnyGenre(Book book, Set<String> genres) {
        return genres.stream()
            .anyMatch(genre -> book.genres().contains(genre));
    }

    private boolean containsAllGenre(Book book, Set<String> genres) {
        return book.genres().containsAll(genres);
    }

}