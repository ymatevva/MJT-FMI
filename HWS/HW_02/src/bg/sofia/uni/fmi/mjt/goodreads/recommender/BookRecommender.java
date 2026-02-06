package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {

    private Set<Book> books;
    private SimilarityCalculator calculator;

    public BookRecommender(Set<Book> books, SimilarityCalculator calculator) {
        this.books = books;
        this.calculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book originBook, int maxN) {
        if (originBook == null) {
            throw new IllegalArgumentException("The origin book cannot be null");
        }

        if (maxN <= 0) {
            throw new IllegalArgumentException(
                "The number of maximum books similar to the original should be positive number.");
        }

        return books.stream()
            .map(book -> Map.entry(book, calculator.calculateSimilarity(book, originBook)))
            .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
            .limit(maxN)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                () -> new TreeMap<>(
                    Comparator.comparingDouble((Book b) -> calculator.calculateSimilarity(b, originBook))
                        .reversed()
                )
            ));
    }
}
