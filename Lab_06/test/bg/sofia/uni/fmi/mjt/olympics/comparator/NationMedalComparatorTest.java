package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class NationMedalComparatorTest {


    // so first we create mock instance of the class used in the constructor of national medal comparator
    // this way we will not be connected to the correctness of this class
    private MJTOlympics mjtOlympicsMock;
    // here we inject it in the constructor of the class to be tested
    private NationMedalComparator nationMedalComparator;

    @BeforeEach
    void setup() {
        mjtOlympicsMock = Mockito.mock(MJTOlympics.class);
        nationMedalComparator = new NationMedalComparator(mjtOlympicsMock);
    }

    @Test
    void testCompareNationsWithFirstNationLessMedals() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(1);
        when(mjtOlympicsMock.getTotalMedals("Nigeria")).thenReturn(2);

        assertEquals(-1, nationMedalComparator.compare("Bulgaria", "Nigeria"),
            "Compare method with first nation with less medals should return -1;");
    }

    @Test
    void testCompareNationsWithSecondNationLessMedals() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(2);
        when(mjtOlympicsMock.getTotalMedals("Nigeria")).thenReturn(1);

        assertEquals(1, nationMedalComparator.compare("Bulgaria", "Nigeria"),
            "Compare method with second nation with less medals should return 1;");
    }

    @Test
    void testCompareNationsWithEqualMedalsFirstSmallerName() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(1);
        when(mjtOlympicsMock.getTotalMedals("Nigeria")).thenReturn(1);

        assertTrue(nationMedalComparator.compare("Bulgaria", "Nigeria") < 0,
            "Compare method with equal medals but first name is smaller than second name lexicographically should return true.");
    }


    @Test
    void testCompareNationsWithEqualMedalsSecondSmallerName() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(1);
        when(mjtOlympicsMock.getTotalMedals("Nigeria")).thenReturn(1);


        assertTrue(nationMedalComparator.compare("Nigeria", "Bulgaria") > 0,
            "Compare method with equal medals but second name is smaller than first name lexicographically should return true.");
    }

    @Test
    void testCompareNationsWithEqualMedalsEqualNames() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(1);
        // when(mjtOlympicsMock.getTotalMedals("Nigeria")).thenReturn(1);

        assertEquals(0, nationMedalComparator.compare("Bulgaria", "Bulgaria"),
            "Compare method with equal medals and equal names lexicographically should return 0.");
    }
}
