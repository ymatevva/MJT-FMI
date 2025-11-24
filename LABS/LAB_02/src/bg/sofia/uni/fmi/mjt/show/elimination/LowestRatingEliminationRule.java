package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowestRatingEliminationRule extends AbstractEliminationRule {

    public LowestRatingEliminationRule() {
    }

    private int getLowestRating(Ergenka[] ergenkas) {
        int lowestRating = Integer.MAX_VALUE;

        for (var ergenka : ergenkas) {
            if (ergenka != null) {
                lowestRating = Math.min(lowestRating, ergenka.getRating());
            }
        }
        return lowestRating;
    }

    @Override
    protected boolean shouldEliminateErgenka(Ergenka ergenka, int criteria) {
        return ergenka.getRating() == criteria;
    }


    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        int lowestRate = getLowestRating(ergenkas);
        int remainingErgenkasCount = countRemainingErgenkas(ergenkas, lowestRate, true);

        Ergenka[] remainedErgenkas = new Ergenka[remainingErgenkasCount];

        int ind = 0;
        for (int i = 0; i < ergenkas.length; i++) {
            if (ergenkas[i] != null) {
                if (!shouldEliminateErgenka(ergenkas[i], lowestRate)) {
                    remainedErgenkas[ind++] = ergenkas[i];
                    if (ind == remainingErgenkasCount) {
                        break;
                    }
                }
            }
        }
        return remainedErgenkas;
    }
}