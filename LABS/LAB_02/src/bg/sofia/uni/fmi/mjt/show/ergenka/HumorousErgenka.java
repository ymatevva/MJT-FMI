package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class HumorousErgenka extends AbstractErgenka {

    public HumorousErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {
        super(name, age, romanceLevel, humorLevel, rating);
    }

    private int getBonus(DateEvent dateEvent) {
        int durationEvent = dateEvent == null ? 0 : dateEvent.getDuration();

        if (durationEvent < THIRTY_MINUTES) {
            return HUMOROUS_BONUS_SHORT_DATE;
        } else if (durationEvent <= NINETY_MINUTES) {
            return HUMOROUS_BONUS_PERFECT_DATE;
        } else if(durationEvent > HUNDRED_TWENTY_MINUTES){
            return HUMOROUS_BONUS_LONG_DATE;
        }
        return 0;
    }

    @Override
    public void reactToDate(DateEvent dateEvent) {
        int tension = dateEvent == null ? 1 : dateEvent.getTensionLevel();
        this.rating += (this.getHumorLevel() * HUMOUR_LEVEL_COEFFICIENT) / tension+
                Math.floorDiv(this.getRomanceLevel(), DIVISION_NUMBER) + getBonus(dateEvent);
    }

}
