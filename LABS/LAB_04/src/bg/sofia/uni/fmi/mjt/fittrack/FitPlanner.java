package bg.sofia.uni.fmi.mjt.fittrack;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;
import bg.sofia.uni.fmi.mjt.fittrack.workout.algorithm.AlgorithmAPI;
import bg.sofia.uni.fmi.mjt.fittrack.workout.algorithm.KnapsackAlgorithm;
import bg.sofia.uni.fmi.mjt.fittrack.workout.comparator.ComparatorCalories;
import bg.sofia.uni.fmi.mjt.fittrack.workout.comparator.ComparatorDifficulty;
import bg.sofia.uni.fmi.mjt.fittrack.workout.filter.WorkoutFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FitPlanner implements FitPlannerAPI {

    private final Set<Workout> availableWorkouts;

    public FitPlanner(Collection<Workout> availableWorkouts) {
        validateAvailableWorkouts(availableWorkouts);
        this.availableWorkouts = new HashSet<>(availableWorkouts);
    }

    @Override
    public List<Workout> findWorkoutsByFilters(List<WorkoutFilter> filters) {
        validateFilters(filters);
        List<Workout> filteredWorkouts = new ArrayList<>();

        for (Workout workout : availableWorkouts) {

            boolean fitsFilters = true;
            for (int i = 0; i < filters.size(); i++) {
                if (!filters.get(i).matches(workout)) {
                    fitsFilters = false;
                    break;
                }
            }
            if (fitsFilters) {
                filteredWorkouts.add(workout);
            }
        }
        return filteredWorkouts;
    }

    @Override
    public List<Workout> generateOptimalWeeklyPlan(int totalMinutes) throws OptimalPlanImpossibleException {
        validateOptimalWeeklyPlan(totalMinutes);
        AlgorithmAPI alg = new KnapsackAlgorithm(availableWorkouts);
        List<Workout> plan =  alg.createOptimalPlan(totalMinutes);
        return plan;
    }

    @Override
    public Map<WorkoutType, List<Workout>> getWorkoutsGroupedByType() {
        Map<WorkoutType, List<Workout>> groupedWorkouts = new HashMap<>();

        for (Workout workout : availableWorkouts) {
            groupedWorkouts.putIfAbsent(workout.getType(), new ArrayList<Workout>());
            groupedWorkouts.get(workout.getType()).add(workout);
        }

        return Collections.unmodifiableMap(groupedWorkouts);
    }

    @Override
    public List<Workout> getWorkoutsSortedByCalories() {
        List<Workout> sortedByCalories = new ArrayList<>(availableWorkouts);
        Collections.sort(sortedByCalories, new ComparatorCalories());
        Collections.reverse(sortedByCalories); 
        return Collections.unmodifiableList(sortedByCalories);
    }

    @Override
    public List<Workout> getWorkoutsSortedByDifficulty() {
        List<Workout> sortedByDifficulty = new ArrayList<>(availableWorkouts);
        Collections.sort(sortedByDifficulty, new ComparatorDifficulty());
        return Collections.unmodifiableList(sortedByDifficulty);
    }

    @Override
    public Set<Workout> getUnmodifiableWorkoutSet() {
        return Collections.unmodifiableSet(availableWorkouts);
    }

    private void validateOptimalWeeklyPlan(int totalMinutes) {
        if (totalMinutes < 0) {
            throw new IllegalArgumentException("The total minutes argument should be a positive number.");
        }
    }

    private void validateAvailableWorkouts(Collection<Workout> availableWorkouts) {
        if (availableWorkouts == null) {
            throw new IllegalArgumentException("The available workouts argument should not be null.");
        }
    }

    private void validateFilters(List<WorkoutFilter> filters) {
        if (filters == null) {
            throw new IllegalArgumentException("The filters should not be null.");
        }
    }
}
