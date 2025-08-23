import bg.sofia.uni.fmi.mjt.goodreads.BookLoader;
import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinder;
import bg.sofia.uni.fmi.mjt.goodreads.finder.MatchOption;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String filePathStopWords = "stopwords.txt";

        Reader readerStopWords = new FileReader(filePathStopWords);
        TextTokenizer textTokenizer = new TextTokenizer(readerStopWords);

        String filePathGoodreadsData =  "goodreads_data.csv";
        Reader readerGoodreadsdata = new FileReader(filePathGoodreadsData);

        Set<Book> books = BookLoader.load(readerGoodreadsdata);
        BookFinder bookFinder = new BookFinder(books, textTokenizer);

        System.out.println("Find by author Agata Christie");
        List<Book> foundByAuthorList = bookFinder.searchByAuthor("Agatha Christie");

        for (var book : foundByAuthorList) {
            System.out.println(book.toString());
        }

        List<Book> foundByKeyword = bookFinder.searchByKeywords(Set.of("pink"), MatchOption.MATCH_ALL);

        for (var book : foundByKeyword) {
            System.out.println(book.title());
        }

        List<Book> foundByGenres = bookFinder.searchByGenres(Set.of("Romance", "Magic"),MatchOption.MATCH_ALL);

        for (var book : foundByGenres) {
            System.out.println(book.title());
        }
    }

}
