package bg.sofia.uni.fmi.mjt.goodreads.comparator;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;

public class ComparatorSimilarity implements Comparator<Book> {

    private final SimilarityCalculator calculator;
    private final Book originBook;

    public ComparatorSimilarity(SimilarityCalculator calculator, Book originBook) {
        this.calculator = calculator;
        this.originBook = originBook;
    }

    @Override
    public int compare(Book o1, Book o2) {
        double similarityWithFirstBook = calculator.calculateSimilarity(o1, originBook);
        double similarityWithSecondBook = calculator.calculateSimilarity(o2, originBook);

        return Double.compare(similarityWithFirstBook, similarityWithSecondBook);
    }
}
