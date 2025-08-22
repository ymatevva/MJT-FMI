package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("The book arguments cannot be null.");
        }

        Set<String> genresFirstBook = new HashSet<>(first.genres());
        Set<String> genresSecondBook = new HashSet<>(second.genres());

        Set<String> intersection = new HashSet<>(genresFirstBook);
        intersection.retainAll(genresSecondBook);

        return intersection.size() / Math.min(genresFirstBook.size(), genresSecondBook.size());
    }

}