package bg.sofia.uni.fmi.mjt.burnout.sort;

import bg.sofia.uni.fmi.mjt.burnout.criteria.Criteria;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public class BubbleSort implements SortingAlg {

    @Override
    public UniversitySubject[] sort(UniversitySubject[] subjects, Criteria criteria) {
        UniversitySubject[] sortedSubjects = subjects;

        int end = sortedSubjects.length - 1;
        for (int i = 0; i < sortedSubjects.length; i++) {

            int lastSwapInd = 0;
            for (int j = 0; j < end; j++) {

                boolean shouldSwap = false;
                switch (criteria) {
                    case RATING -> {
                        if (sortedSubjects[j].rating() < sortedSubjects[j + 1].rating()) {
                            shouldSwap = true;
                        }
                    }
                    case CREDITS -> {
                        if (sortedSubjects[j].credits() < sortedSubjects[j + 1].credits()) {
                            shouldSwap = true;
                        }
                    }
                }

                if(shouldSwap) {
                    UniversitySubject temp = sortedSubjects[j];
                    sortedSubjects[j] = sortedSubjects[j + 1];
                    sortedSubjects[j + 1] = temp;
                    lastSwapInd = j;
                }
            }
            if (lastSwapInd == 0) {
                return sortedSubjects;
            }
            end = lastSwapInd;
        }
        return sortedSubjects;
    }
}
