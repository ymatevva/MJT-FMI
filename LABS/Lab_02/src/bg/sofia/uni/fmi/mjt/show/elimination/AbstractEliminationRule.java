package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public abstract class AbstractEliminationRule implements EliminationRule {

    protected int countRemainingErgenkas(Ergenka[] ergenkas, int criteria, boolean removeNulls) {
        int remaining = 0;
        for (int i = 0; i < ergenkas.length; i++) {
            if(ergenkas[i] == null) {
                if(removeNulls){
                    continue;
                }
                remaining++;
            }
            else if (!shouldEliminateErgenka(ergenkas[i], criteria)) {
                remaining++;
            }
        }
        return remaining;
    }

    protected abstract boolean shouldEliminateErgenka(Ergenka ergenka, int criteria);
}