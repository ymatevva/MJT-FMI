package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private Set<Book> books;
    private TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        setBooks(books);
        this.tokenizer = tokenizer;
    }

    public void setBooks(Set<Book> books) {
        if (books == null) {
            throw new IllegalArgumentException("The set of books cannot be null.");
        }
        this.books = books;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("The book arguments cannot be null");
        }

        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {

        Map<String, Double> tfValue = computeTF(book);
        Map<String, Double> idfValue = computeIDF(book);

        return tokenizer.tokenize(book.description()).stream()
            .map(word -> Map.entry(word, tfValue.get(word) * idfValue.get(word)))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing
            ));
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> tokens = tokenizer.tokenize(book.description()); // tokenize once
        int totalTokens = tokens.size();
        // should return map with all words in the description and their count of occurrence
        return tokens.stream()
            .collect(Collectors.groupingBy(word -> word,
                Collectors.collectingAndThen(Collectors.counting(), count -> (double)count / totalTokens)));
    }

    public Map<String, Double> computeIDF(Book book) {
        return tokenizer.tokenize(book.description()).stream()
            .map(word -> Map.entry(word, Math.log((double) books.size() / (1 + countBooksInWhichOccurred(word)))))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing)
            );
    }

    private double countBooksInWhichOccurred(String word) {
        return (double) (books.stream()
            .filter(book -> tokenizer.tokenize(book.description()).contains(word))
            .count());
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
}