import bg.sofia.uni.fmi.mjt.goodreads.BookLoader;
import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinder;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
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
        List<Book> foundByAuthorList = bookFinder.searchByAuthor("Agata Christie");

        for (var book : books) {
            System.out.println(book.toString());
        }

    }
}
