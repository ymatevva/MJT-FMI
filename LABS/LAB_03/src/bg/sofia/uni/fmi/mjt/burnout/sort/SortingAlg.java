package bg.sofia.uni.fmi.mjt.burnout.sort;

import bg.sofia.uni.fmi.mjt.burnout.criteria.Criteria;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public interface SortingAlg {

    public UniversitySubject[] sort(UniversitySubject[] subjects, Criteria criteria);
}
