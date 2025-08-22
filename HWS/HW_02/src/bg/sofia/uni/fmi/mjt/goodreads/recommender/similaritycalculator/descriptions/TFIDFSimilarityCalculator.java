package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private Set<Book> books;
    private TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
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
                Map.Entry::getValue
            ));
    }

    public Map<String, Double> computeTF(Book book) {

        // should return map with all words in the description and their count of occurrence

        return tokenizer.tokenize(book.description()).stream()
            .collect(Collectors.groupingBy(word -> word,
                Collectors.collectingAndThen(Collectors.counting(), count -> count.doubleValue())));
    }

    public Map<String, Double> computeIDF(Book book) {
        return tokenizer.tokenize(book.description()).stream()
            .map(word -> Map.entry(word, Math.log((double) countBooksInWhichOccurred(word) / books.size())))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue)
            );
    }

    private int countBooksInWhichOccurred(String word) {
        return (int) (books.stream()
            .filter(book -> book.description().contains(word))
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