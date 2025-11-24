package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.CryToStudentsDepartmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.criteria.Criteria;
import bg.sofia.uni.fmi.mjt.burnout.subject.Category;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;

public non-sealed class SoftwareEngineeringSemesterPlanner extends AbstractSemesterPlanner {

    private int addExtraSubjects(UniversitySubject[] sortedSubjects, int creditsNeeded, boolean[] addedSubjectsMarker){
        int countSubjects = 0;
        for (int i = 0; i < sortedSubjects.length; i++) {
            if (creditsNeeded <= 0) {
                break;
            }
            if (addedSubjectsMarker[i] == false) {
                addedSubjectsMarker[i] = true;
                creditsNeeded -= sortedSubjects[i].credits();
                countSubjects++;
            }
        }
        if (creditsNeeded > 0) {
            throw new CryToStudentsDepartmentException("Impossible credit requirement.");
        }
        return countSubjects;
    }
    private int markSubjectsInList(boolean[] addedSubjectsMarker, SemesterPlan semesterPlan, UniversitySubject[] sortedSubjects) {
        int creditsNeeded = semesterPlan.minimalAmountOfCredits();
        int countSubjects = 0;

        // we loop through all requirements
        for (int i = 0; i < semesterPlan.subjectRequirements().length; i++) {

            // this is the current requirement - Category and count of subjects from this category

            SubjectRequirement currReq = semesterPlan.subjectRequirements()[i];
            int subjectsFromCategory = currReq.minAmountEnrolled();
            Category currCat = currReq.category();

            for (int j = 0; j < sortedSubjects.length; j++) {

                if (subjectsFromCategory == 0) {
                    break;
                }

                if (sortedSubjects[j].category() == currCat) {
                    addedSubjectsMarker[j] = true;
                    creditsNeeded -= sortedSubjects[j].credits();
                    subjectsFromCategory--;
                    countSubjects++;
                }
            }
        }

        if (creditsNeeded > 0) {
           countSubjects +=  addExtraSubjects(sortedSubjects, creditsNeeded, addedSubjectsMarker);
        }

        return countSubjects;
    }


    // subjects are sorted by credits The first is the one with the most credits
    @Override
    public UniversitySubject[] calculateSubjectList(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {

        if (semesterPlan == null ) {
            throw new IllegalArgumentException("The semester plan cannot be null.");
        }

        checkForCategoryDuplicates(semesterPlan);
        UniversitySubject[] sortedSubjects = sortingAlg.sort(semesterPlan.subjects(), Criteria.CREDITS);
        boolean[] addedSubjectsMarker = new boolean[sortedSubjects.length];
        int countSubjects = markSubjectsInList(addedSubjectsMarker, semesterPlan, sortedSubjects);

        UniversitySubject[] subjectsList = new UniversitySubject[countSubjects];
        int index = 0;

        for (int i = 0; i < addedSubjectsMarker.length; i++) {
            if (addedSubjectsMarker[i] != false) {
                subjectsList[index++] = sortedSubjects[i];
            }
        }

        return subjectsList;
    }
}
