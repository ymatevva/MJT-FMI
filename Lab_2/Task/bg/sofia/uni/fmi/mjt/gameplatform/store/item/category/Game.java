package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class Game implements StoreItem{

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private String genre;
    private double rating;
    private int rates;


    
    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
      setPrice(price);
      setReleaseDate(releaseDate);
      setTitle(title);
      setGenre(genre);
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
      return rating/rates;
    }

    @Override
    public LocalDateTime getReleaseDate() {
       return releaseDate;
    }

    @Override
    public String getTitle() {
       return title;
    }

    @Override
    public void rate(double rating) {
        if(rating >= 1.0 || rating <= 5.0){
            this.rating += rating;
            rates++;
        }
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price.compareTo(BigDecimal.valueOf(0.0)) == -1?  BigDecimal.valueOf(0.0): price.setScale(2,RoundingMode.HALF_UP);
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title == null || title.isEmpty() ? "Unknown" : title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre == null || genre.isEmpty() ? "Unknown" : genre;
    }

    
}
