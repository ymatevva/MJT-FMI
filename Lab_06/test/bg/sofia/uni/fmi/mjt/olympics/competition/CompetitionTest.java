package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionTest {


    private Set<Competitor> competitors;

    @BeforeEach
    void setup() {
        Competitor competitor1 = Mockito.mock(Competitor.class);
        Competitor competitor2 = Mockito.mock(Competitor.class);
        competitors = new HashSet<>();

        competitors.add(competitor1);
        competitors.add(competitor2);
    }

    @Test
    void testWhenNameIsNullOrIsBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("", "SWIM", competitors),
            "The name for the competition cannot be blank.");
        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "SWIM", competitors),
            "The name for the competition cannot be null.");
    }

    @Test
    void testWhenDisciplineIsNullOrIsBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Y", "", competitors),
            "The discipline for the competition cannot be blank.");
        assertThrows(IllegalArgumentException.class, () -> new Competition("A", null, competitors),
            "The discipline for the competition cannot be null.");
    }

    @Test
    void testWhenCompetitionIsNullOrNoCompetitors() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("Y", "SWIM", null),
            "The competitors set is null.");
        assertThrows(IllegalArgumentException.class, () -> new Competition("A", "SWIM", new HashSet<>()),
            "There is no competitors for the competition .");
    }

    @Test
    void testGetCompetitorsReturnsUnmodifiableSet() {
        Competition competition = new Competition("NAME", "DISC", competitors);
        Set<Competitor> copySet = competition.competitors();
        Competitor competitor3 = Mockito.mock();

        assertThrows(UnsupportedOperationException.class, () -> copySet.add(competitor3),
            "Method for getting competitors should return unmodifiable collection.");
    }


    @Test
    void testEqualsWhenObjectIsNull() {
        Competition competition = new Competition("NAME", "DISC", competitors);
        assertFalse(competition.equals(null), "Equals should return false for argument null.");
    }

    @Test
    void testEqualsWhenObjectIsSame() {
        Competition competition = new Competition("NAME", "DISC", competitors);
        assertTrue(competition.equals(competition), "Equals should return true for same competition.");
    }

    @Test
    void testEqualsWhenObjectIsNotFromSameClass() {
        Competition competition = new Competition("NAME", "DISC", competitors);
        assertFalse(competition.equals(String.class), "Equals should return false for argument which is not from the same class.");
    }

    @Test
    void testEqualObjectsWithSameHash() {
        Competition competition1 = new Competition("NAME", "DISC", competitors);
        Competition competition2 = new Competition("NAME", "DISC", competitors);

        assertTrue(competition1.equals(competition2), "Equals should be true for objects with same data.");
        assertTrue(competition2.equals(competition1), "Equals should be true for objects with same data.");

        assertTrue(competition2.hashCode() == competition1.hashCode(), "Hash codes should be same for objects with same data.");
    }

    @Test
    void testEqualWithObjectsWithDifferentCompetitors() {

        Competition competition1 = new Competition("NAME", "DISC", competitors);
        Set<Competitor> competitors2 = new HashSet<>();

        Competitor competitor3 = Mockito.mock(Competitor.class);
        competitors2.add(competitor3);
        Competition competition2 = new Competition("NAME", "DISC", competitors2);

        assertFalse(competition1.equals(competition2), "Competitions with same name and discipline, but different competitors should not be equal.");
        assertFalse(competition1.hashCode() == competition2.hashCode(), "Competitions with same name and discipline, but different competitors should have different hash codes.");
    }
}
