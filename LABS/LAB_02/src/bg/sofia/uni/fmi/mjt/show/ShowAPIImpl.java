package bg.sofia.uni.fmi.mjt.show;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.AbstractEliminationRule;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.elimination.LowestRatingEliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class ShowAPIImpl implements ShowAPI {

    private Ergenka[] ergenkas;
    private final EliminationRule[] defaultRules = {new LowestRatingEliminationRule()};
    private EliminationRule[] constructorDefaultEliminationRules;

    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {

        this.ergenkas = ergenkas;

        if (defaultEliminationRules == null || defaultEliminationRules.length == 0) {
            this.constructorDefaultEliminationRules = this.defaultRules;
        } else {
            this.constructorDefaultEliminationRules = defaultEliminationRules;
        }
    }

    @Override
    public Ergenka[] getErgenkas() {
        return ergenkas;
    }

    @Override
    public void playRound(DateEvent dateEvent) {

        if (ergenkas == null) {
            return;
        }

        for (var ergenka : ergenkas) {
            if (ergenka != null) {
                ergenka.reactToDate(dateEvent);
            }
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {

        if (ergenkas == null) {
            this.ergenkas = new Ergenka[0];
        }

        if (eliminationRules == null || eliminationRules.length == 0) {
            eliminationRules = this.constructorDefaultEliminationRules;
        }

        for (int i = 0; i < eliminationRules.length; i++) {
            if (eliminationRules[i] != null) {
                Ergenka[] updatedErgenkas = eliminationRules[i].eliminateErgenkas(ergenkas);
                ergenkas = (updatedErgenkas != null) ? updatedErgenkas : new Ergenka[0];
            }
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        if (ergenka == null) {
            return;
        }
        ergenka.reactToDate(dateEvent);
    }
}