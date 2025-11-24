package bg.sofia.uni.fmi.mjt.burnout.semester;

import bg.sofia.uni.fmi.mjt.burnout.exception.DisappointmentException;
import bg.sofia.uni.fmi.mjt.burnout.exception.InvalidSubjectRequirementsException;
import bg.sofia.uni.fmi.mjt.burnout.plan.SemesterPlan;
import bg.sofia.uni.fmi.mjt.burnout.criteria.Criteria;
import bg.sofia.uni.fmi.mjt.burnout.sort.BubbleSort;
import bg.sofia.uni.fmi.mjt.burnout.sort.SortingAlg;
import bg.sofia.uni.fmi.mjt.burnout.subject.SubjectRequirement;
import bg.sofia.uni.fmi.mjt.burnout.subject.UniversitySubject;


public abstract sealed class AbstractSemesterPlanner implements SemesterPlannerAPI permits ComputerScienceSemesterPlanner, SoftwareEngineeringSemesterPlanner{

    protected final SortingAlg sortingAlg = new BubbleSort();

    protected void checkForCategoryDuplicates(SemesterPlan semesterPlan) throws InvalidSubjectRequirementsException {
        SubjectRequirement[] subjectRequirements = semesterPlan.subjectRequirements();
        for (int i = 0; i < subjectRequirements.length; i++) {
            for (int j = i + 1; j < subjectRequirements.length; j++) {
                if (subjectRequirements[i].category() == subjectRequirements[j].category()) {
                    throw new InvalidSubjectRequirementsException("The required categories cannot have duplicates.");
                }
            }
        }
    }

    private int calculateWorkTime(UniversitySubject[] subjects) {
        int workTime = 0;
        for(UniversitySubject subject : subjects){
            workTime += subject.neededStudyTime();
        }
        return workTime;
    }

    private int calculateSlackTime(UniversitySubject[] subjects) {
        int slackTime = 0;

        for (UniversitySubject subject : subjects) {
            slackTime += subject.neededStudyTime() * subject.category().getRestCoeff();
        }

        return slackTime;
    }

    @Override
    public int calculateJarCount(UniversitySubject[] subjects, int maximumSlackTime, int semesterDuration) {
        if ( subjects == null || subjects.length == 0) {
            throw new IllegalArgumentException("The argument for subjects is either missing or null.");
        }

        if (maximumSlackTime <= 0) {
            throw new IllegalArgumentException("The maximumSlackTime should be a positive number.");
        }

        if (semesterDuration <= 0) {
            throw new IllegalArgumentException("The semesterDuration should be a posotive number.");
        }

        int slackTime = calculateSlackTime(subjects);
        if (slackTime > maximumSlackTime) {
            throw new DisappointmentException("Your babushka is mad at you. The slack time is over the maximum.");
        }

        int workTime = calculateWorkTime(subjects);
        int jarCount = workTime / 5;

        // if time is not enough for work and rest then double dose of jars
        if (workTime + slackTime > semesterDuration) {
            jarCount += jarCount;
        }
        return jarCount;
    }

}
