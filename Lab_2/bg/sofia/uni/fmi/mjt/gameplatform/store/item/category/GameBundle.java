package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime; 

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class GameBundle implements StoreItem {

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private Game[] games;
    private double rating;
    private int rates;

    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games) {
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
        setGames(games);
    }

    public Game[] getGames() {
        Game[] copyGames = new Game[games.length];
        
        for (int i = 0; i < copyGames.length; i++) {
            copyGames[i] = games[i];
        }
        return copyGames;
    }

    public void setGames(Game[] games) {
        if(games == null) {
            return;
        }
        this.games = new Game[games.length];
        System.arraycopy(games, 0, this.games, 0, games.length);
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

}
