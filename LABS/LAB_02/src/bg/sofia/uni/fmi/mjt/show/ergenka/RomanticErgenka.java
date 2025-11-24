package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class RomanticErgenka extends AbstractErgenka {

    private final String favoriteDateLocation;

    public RomanticErgenka(String name, short age, int romanceLevel, int humorLevel, int rating, String favoriteDateLocation) {
        super(name, age, romanceLevel, humorLevel, rating);
        this.favoriteDateLocation = favoriteDateLocation;
    }

    private int getBonus(DateEvent dateEvent) {
        int totalBonus = 0;
        int durationEvent = dateEvent == null ? 0 : dateEvent.getDuration();
        String eventLocation = dateEvent == null ? "" : dateEvent.getLocation();

        if (this.favoriteDateLocation != null && this.favoriteDateLocation.toLowerCase().equals(eventLocation.toLowerCase())) {
            totalBonus += ROMANTIC_BONUS_FAV_PLACE;
        }
        if (durationEvent < THIRTY_MINUTES) {
            totalBonus += ROMANTIC_BONUS_SHORT_DATE;
        } else if (durationEvent > HUNDRED_TWENTY_MINUTES) {
            totalBonus += ROMANTIC_BONUS_LONG_DATE;
        }
        return totalBonus;
    }

    @Override
    public void reactToDate(DateEvent dateEvent) {
        int tension = dateEvent == null ? 1 : dateEvent.getTensionLevel();
        this.rating += ((this.getRomanceLevel() * ROMANTIC_LEVEL_COEFFICIENT) / tension) +
                Math.floorDiv(this.getHumorLevel(), DIVISION_NUMBER) + getBonus(dateEvent);

    }
}
