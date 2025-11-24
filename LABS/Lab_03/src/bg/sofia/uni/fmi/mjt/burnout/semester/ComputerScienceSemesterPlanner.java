package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.criteria.Criteria;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public non-sealed class ComputerScienceSemesterPlanner extends AbstractSemesterPlanner{

    // calculates how many subjects we need at least to cover the requirements
    // the subjects are sorted from best to worst rated
    // the moment we gather enough credits we stop adding more
    // the cs student does not care about subjects by category

    private int countSubjectsInList(SemesterPlan semesterPlan, UniversitySubject[] subjects) {
        int count = 0;
        int minimumCredits = semesterPlan.minimalAmountOfCredits();

        for (int i = 0; i < subjects.length; i++) {
            if(minimumCredits - subjects[i].credits() <= 0){
                return count + 1;
            }
            minimumCredits -= subjects[i].credits();
            count++;
        }
        if (minimumCredits > 0) {
            throw new CryToStudentsDepartmentException("Impossible credit requirement.");
        }
        return count;
    }


    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {
        if (semesterPlan == null ) {
            throw new IllegalArgumentException("The semester plan cannot be null.");
        }
        checkForCategoryDuplicates(semesterPlan);
        UniversitySubject[] sortedSubjects = sortingAlg.sort(semesterPlan.subjects(), Criteria.RATING);
        int subjectsInListCount = countSubjectsInList(semesterPlan, sortedSubjects);

        UniversitySubject[] subjectsList = new UniversitySubject[subjectsInListCount];

        System.arraycopy(sortedSubjects,0,subjectsList,0,subjectsInListCount);
        return subjectsList;
    }
}
