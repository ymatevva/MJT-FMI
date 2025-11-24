package bg.sofia.uni.fmi.mjt.fittrack.workout.algorithm;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

import java.util.List;

public interface AlgorithmAPI {

    public List<Workout> createOptimalPlan(int totalMinutes) throws OptimalPlanImpossibleException;
}
