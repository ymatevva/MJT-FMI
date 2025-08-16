package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bg.sofia.uni.fmi.mjt.football.exception.NoSuchElementException;

public class FootballPlayerAnalyzer {

    private List<Player> players;

    public FootballPlayerAnalyzer(Reader reader) {
        readFile(reader);
    }

    public List<Player> getAllPlayers() {
        return players.stream()
            .collect(Collectors.toUnmodifiableList());
    }

    public Set<String> getAllNationalities() {
        return players.stream()
            .map(Player::nationality)
            .distinct()
            .collect(Collectors.toUnmodifiableSet());
    }

    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("The nationality provided is null.");
        }

        return players.stream()
            .filter(player -> player.nationality().equals(nationality))
            .max(Comparator.comparingDouble(Player::wageEuro))
            .orElseThrow(() -> new NoSuchElementException("Player with the given nationality does not exist."));
    }

    /**
     * Returns a breakdown of players by position. Note that some players can play in more than one position so they
     * should be present in more than one value Set. If no player plays in a given Position then that position should
     * not be present as a key in the map.
     *
     * @return a Map with key: a Position and value: the set of players in the dataset that can play in that Position,
     * in undefined order.
     */
     public Map<Position, Set<Player>> groupByPosition() {
        return players.stream()
            .flatMap(player -> player.positions().stream() // the positions a player takes
                .map(position -> Map.entry(position, player))) // for each position a map element <Position,PLayer>
            .collect(Collectors.groupingBy( //then for key(Poistion) collecting its values and makin set
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
            ));
    }

 // Option 1:
//    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
//        if (position == null || budget < 0) {
//            throw new IllegalArgumentException("The position is null or the budget is a negative number.");
//        }
//
//     return players.stream()
//            .filter( player -> player.positions().contains(position)
//            && player.valueEuro() <= budget)
//            .max(Comparator.comparingDouble(p -> (p.overallRating() + p.potential()) / p.age()));
//    }

    // Option 2:
    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null || budget < 0) {
            throw new IllegalArgumentException("The position is null or the budget is a negative number.");
        }

        return players.stream()
            .filter( player -> player.positions().contains(position)
                && player.valueEuro() <= budget)
            .max(Comparator.comparingDouble(this::calcProspect));
    }

    private double calcProspect(Player player) {
        return (double)(player.overallRating() + player.potential()/ player.age());
    }

    public Set<Player> getSimilarPlayers(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("The provided player cannot be null.");
        }

        return players.stream()
            .filter(otherPlayer ->
                otherPlayer.positions().stream().anyMatch(player.positions()::contains)
                    && otherPlayer.preferredFoot().equals(player.positions())
                    && Math.abs(otherPlayer.overallRating() - player.overallRating()) <= 3)
            .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("The keyword cannot be null.");
        }

        return players.stream()
            .filter(player -> player.fullName().contains(keyword))
            .collect(Collectors.toUnmodifiableSet());
    }

    private void readFile(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> currLine = bufferedReader.lines()) {
             currLine.skip(1).forEach(line -> {
                players.add(Player.of(line));
            });
        } catch (IOException e) {
            throw new RuntimeException("Exception thrown while reading from file.");
        }
    }
}


