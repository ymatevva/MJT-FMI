package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @throws IllegalArgumentException when creating a competition with null or blank name
 * @throws IllegalArgumentException when creating a competition with null or blank discipline
 * @throws IllegalArgumentException when creating a competition with null or empty competitors
 */
public record Competition(String name, String discipline, Set<Competitor> competitors) {

    public Competition {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name is null or is blank.");
        }

        if (discipline == null || discipline.isBlank()) {
            throw new IllegalArgumentException("The discipline cannot be null not blank.");
        }

        if (competitors == null || competitors.isEmpty()) {
            throw new IllegalArgumentException("The competitors set is empty or is null.");
        }

    }

    public Set<Competitor> competitors() {
        return Collections.unmodifiableSet(competitors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competition that = (Competition) o;
        return Objects.equals(name, that.name) && Objects.equals(discipline, that.discipline)
            && Objects.equals(competitors,((Competition) o).competitors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discipline, competitors);
    }
}