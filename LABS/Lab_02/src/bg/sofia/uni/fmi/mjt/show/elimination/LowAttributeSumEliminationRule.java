package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

import java.util.Arrays;

public class LowAttributeSumEliminationRule extends AbstractEliminationRule {

    private final int threshold;

    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected boolean shouldEliminateErgenka(Ergenka ergenka, int criteria) {
        return ergenka.getHumorLevel() + ergenka.getRomanceLevel() < criteria;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {

        int remainingErgenkasCount = countRemainingErgenkas(ergenkas, threshold, false);

        Ergenka[] remainedErgenkas = new Ergenka[remainingErgenkasCount];

        int ind = 0;
        int countNulls = 0;

        for (int i = 0; i < ergenkas.length; i++) {
            if(ergenkas[i] == null) {
                countNulls++;
            }
            else if (!shouldEliminateErgenka(ergenkas[i], threshold)) {
                remainedErgenkas[ind++] = ergenkas[i];

                if (ind == remainingErgenkasCount) {
                    break;
                }
            }
        }

        for (int i = 0; i < countNulls ; i++) {
            remainedErgenkas[ind++] =null;
        }
        return remainedErgenkas;
    }
}
