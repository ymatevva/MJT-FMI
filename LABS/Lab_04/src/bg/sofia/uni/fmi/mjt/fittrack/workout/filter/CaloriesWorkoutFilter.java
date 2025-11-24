package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class CaloriesWorkoutFilter implements WorkoutFilter {
    private final int min;
    private final int max;

    public CaloriesWorkoutFilter(int min, int max) {
        validateData(min, max);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(Workout workout) {
        return workout.getCaloriesBurned() >= min && workout.getCaloriesBurned() <= max;
    }

    private void validateData(int min, int max) {
        if (min < 0) {
            throw new IllegalArgumentException("The minimum calories can not be a negative number.");
        }

        if (max < 0) {
            throw new IllegalArgumentException("The maximum calories can not be a negative number.");
        }

        if (min > max) {
            throw new IllegalArgumentException("The minimum calories argument cannot be less that the maximum one.");
        }
    }
}
