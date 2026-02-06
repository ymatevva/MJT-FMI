package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import java.util.Map;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("The book arguments cannot be null.");
        }

        double totalSimilarity = 0;
        for (var calculatorMapEl : similarityCalculatorMap.entrySet()) {
            totalSimilarity += calculatorMapEl.getKey().calculateSimilarity(first, second) * calculatorMapEl.getValue();
        }

        return totalSimilarity;
    }

}