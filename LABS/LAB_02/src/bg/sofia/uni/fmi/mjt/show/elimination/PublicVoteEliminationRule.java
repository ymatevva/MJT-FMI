package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class PublicVoteEliminationRule implements EliminationRule {

    private String[] votes;

    public PublicVoteEliminationRule(String[] votes) {
        this.votes = votes;
    }

    private String checkForStrictMajority(String[] votes, String ergenkaToBeEliminated) {
        int voteCount = 0;
        int nonNullVotes = 0;

        for (String vote : votes) {
            if (vote == null) {
                continue;
            }
            nonNullVotes++;
            if (ergenkaToBeEliminated.equals(vote)) {
                voteCount++;
            }
        }

        return (voteCount > nonNullVotes / 2) ? ergenkaToBeEliminated : null;
    }

    private String getErgenkaToBeEliminated(String[] votes) {
        String ergenkaToBeEliminated = "";
        int counter = 0;

        for (int i = 0; i < votes.length; i++) {
            if(votes[i] == null) {
                continue;
            }
            if (counter == 0) {
                ergenkaToBeEliminated = votes[i];
                counter++;
            } else if (ergenkaToBeEliminated.equals(votes[i])) {
                counter++;
            } else {
                counter--;
            }
        }

        if (ergenkaToBeEliminated != null) {
            ergenkaToBeEliminated = checkForStrictMajority(votes, ergenkaToBeEliminated);
        }
        return ergenkaToBeEliminated;
    }

    private boolean isErgenkaInShow(Ergenka[] ergenkas, String ergenkaName) {
        for (int i = 0; i < ergenkas.length; i++) {
            if(ergenkas[i] == null) {
                continue;
            }
            if (ergenkas[i].getName().equals(ergenkaName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {

        if (votes == null) {
            return ergenkas;
        }

        String ergenkaToBeEliminated = getErgenkaToBeEliminated(votes);
        if (!isErgenkaInShow(ergenkas, ergenkaToBeEliminated)) {
            return ergenkas;
        }

        Ergenka[] remainedErgenkas = new Ergenka[ergenkas.length - 1];
        int ind = 0;

        for (int i = 0; i < ergenkas.length; i++) {
            if(ergenkas[i] == null) {
                remainedErgenkas[ind++] = null;
            }
            else if (ergenkas[i].getName().equals(ergenkaToBeEliminated)) {
                continue;
            }
            else {
                remainedErgenkas[ind++] = ergenkas[i];
            }
        }

        return remainedErgenkas;
    }
}
