package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public abstract class AbstractErgenka implements Ergenka {

    protected final static int ROMANTIC_LEVEL_COEFFICIENT = 7;
    protected final static int HUMOUR_LEVEL_COEFFICIENT = 5;
    protected final static int DIVISION_NUMBER = 3;

    protected final static int ROMANTIC_BONUS_FAV_PLACE = 5;
    protected final static int ROMANTIC_BONUS_SHORT_DATE = -3;
    protected final static int ROMANTIC_BONUS_LONG_DATE = -2;

    protected final static int HUMOROUS_BONUS_PERFECT_DATE = 4;
    protected final static int HUMOROUS_BONUS_SHORT_DATE = -2;
    protected final static int HUMOROUS_BONUS_LONG_DATE = -3;

    protected final static int THIRTY_MINUTES = 30;
    protected final static int NINETY_MINUTES = 90;
    protected final static int HUNDRED_TWENTY_MINUTES = 120;

    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    protected int rating;

    public AbstractErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {
        this.name = name;
        this.age = age;
        this.humorLevel = humorLevel;
        this.romanceLevel = romanceLevel;
        this.rating = rating;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public short getAge() {
        return this.age;
    }

    @Override
    public int getRomanceLevel() {
        return this.romanceLevel;
    }

    @Override
    public int getHumorLevel() {
        return this.humorLevel;
    }

    @Override
    public int getRating() {
        return this.rating;
    }

    public abstract void reactToDate(DateEvent dateEvent);
}
