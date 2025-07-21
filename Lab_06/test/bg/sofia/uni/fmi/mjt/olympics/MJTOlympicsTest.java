package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.comparator.NationMedalComparator;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MJTOlympicsTest {


    private class CustomCoparator implements Comparator<Competitor> {
        @Override
        public int compare(Competitor o1, Competitor o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private CompetitionResultFetcher competitionResultFetcher;
    private MJTOlympics mjtOlympics;
    private Competitor[] competitors;
    private Competition competition;

    @BeforeEach
    void setup() {
        competitors = new Competitor[3];
        competitors[0] = Mockito.mock(Competitor.class);
        when( competitors[0].getName()).thenReturn("NAME1");
        when( competitors[0].getNationality()).thenReturn("NAT1");

        competitors[1] = Mockito.mock(Competitor.class);
        when(competitors[1].getName()).thenReturn("NAME2");
        when(competitors[1].getNationality()).thenReturn("NAT1");

        competitors[2] = Mockito.mock(Competitor.class);
        when(competitors[2].getName()).thenReturn("NAME3");
        when(competitors[2].getNationality()).thenReturn("NAT3");

        TreeSet<Competitor> results = new TreeSet<>(new CustomCoparator());
        results.add(competitors[0]);
        results.add(competitors[1]);
        results.add(competitors[2]);

        competition = Mockito.mock(Competition.class);
        when(competition.competitors()).thenReturn(Set.of(competitors[0], competitors[1], competitors[2]));

        CompetitionResultFetcher fetcher = Mockito.mock(CompetitionResultFetcher.class);
        when(fetcher.getResult(competition)).thenReturn(results);

         mjtOlympics = new MJTOlympics(Set.of(competitors[0], competitors[1], competitors[2]), fetcher);
    }

    @Test
    void testUpdateMedalStatisticsWithNullCompetition() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(null),
            "Updating medal statistic with null competition should throw.");
    }

    @Test
    void testUpdateMedalStatisticWithNotRegisteredCompetitor() {
        Competition competition = Mockito.mock();
        Competitor comp1 = Mockito.mock();
        when(comp1.getIdentifier()).thenReturn("ID1");

       // when(mjtOlympics.getRegisteredCompetitors().contains(comp1)).thenReturn(false);
        when(competition.competitors()).thenReturn(Set.of(comp1));

        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(competition),
            "Update medal statistic should throw if the competitor is not listed in the olympics.");
    }

    @Test
    void testGetTotalMedalsWhenNationalityIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals(null),
            "Get total medals method should throw with nationality which is null.");
    }

    @Test
    void testGetTotalMedalsWhenNationalityIsNotRegistered() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals("smth"),
            "Get total medals method should throw with nationality which is not registered.");
    }

    @Test
    void testUpdateMedalStatistics() {

        assertDoesNotThrow(() -> mjtOlympics.updateMedalStatistics(competition));

        verify(competitors[0]).addMedal(Medal.GOLD);
        verify(competitors[1]).addMedal(Medal.SILVER);
        verify(competitors[2]).addMedal(Medal.BRONZE);

        assertEquals(2,mjtOlympics.getTotalMedals("NAT1"));
        assertEquals(1,mjtOlympics.getTotalMedals("NAT3"));


    }
}