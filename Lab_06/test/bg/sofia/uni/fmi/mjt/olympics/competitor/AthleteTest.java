package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {

    private Athlete athlete1;

    @BeforeEach
    void setup() {
        athlete1 = new Athlete("ID", "NAME", "NAT");
    }

    @Test
    void testAddMedalWithNullMedalThrows() {
        assertThrows(IllegalArgumentException.class, () -> athlete1.addMedal(null),
            "Null medals are not legal argument and the method should throw.");
    }

    @Test
    void testAddMedalWithMedalDoesNoThrow() {
        assertDoesNotThrow(() -> athlete1.addMedal(Medal.BRONZE),
            "AddMedal should not throw when adding bronze medal.");
    }

    @Test
    void testAddMedalWithMedalAddsMedal() {
        // After adding, check the medals collection contains BRONZE
        athlete1.addMedal(Medal.BRONZE);
        assertTrue(athlete1.getMedals().contains(Medal.BRONZE), "Bronze medal should be added.");
    }

    @Test
    void testAddTwoMedalsFromSameCategory() {
        athlete1.addMedal(Medal.BRONZE);
        athlete1.addMedal(Medal.BRONZE);
        // we should not use set
        assertTrue(athlete1.getMedals().size() == 2,
            String.format("After adding two bronze medals the count of medals should be two. But it is %d",
                athlete1.getMedals().size()));
    }

    @Test
    void testEqualsWhenNull() {
        assertFalse(athlete1.equals(null), "Equals should return false when object is compared to null.");
    }

    @Test
    void testWhenObjectIsFromAnotherClass() {
        assertFalse(athlete1.equals(String.class),
            "Equals should return false when object is compared to object from different class.");
    }

    @Test
    void testEqualWithObjectWithSameData() {
        Athlete athlete2 = new Athlete("ID", "NAME", "NAT");

        athlete1.addMedal(Medal.BRONZE);
        athlete2.addMedal(Medal.SILVER);

        assertTrue(athlete1.equals(athlete2), "Athletes with same data should be equal.");
        assertTrue(athlete2.equals(athlete1), "Athletes with same data should be equal.");
        assertEquals(athlete1.hashCode(), athlete2.hashCode(), "Athletes which are equal should have same hash.");
    }



    @Test
    void testEqualWithObjectWithDifferentID() {
        Athlete athlete2 = new Athlete("ID2", "NAME", "NAT");

        assertFalse(athlete1.equals(athlete2), "Athletes with diff ID should not be equal.");
        assertFalse(athlete2.equals(athlete1), "Athletes with diff ID should not be equal.");
    }

    @Test
    void testUnmodifiableCollectionOfMedals() {
        athlete1.addMedal(Medal.BRONZE);
        athlete1.addMedal(Medal.SILVER);

        Collection<Medal> copyMedals = athlete1.getMedals();

        assertThrows(UnsupportedOperationException.class, () -> copyMedals.add(Medal.GOLD),
            "Athlete's medals should not be affected by change of copy collection of the medals.");
    }

}