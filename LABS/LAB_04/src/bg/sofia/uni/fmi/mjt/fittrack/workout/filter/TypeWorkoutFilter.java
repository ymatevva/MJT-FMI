package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;

public class TypeWorkoutFilter implements WorkoutFilter {
    private final WorkoutType type;

    public TypeWorkoutFilter(WorkoutType type) {
        validateData(type);
        this.type = type;
    }

    @Override
    public boolean matches(Workout workout) {
        return workout.getType() == this.type;
    }

    private void validateData(WorkoutType type) {
        if (type == null) {
            throw new IllegalArgumentException("The workout type cannot be null.");
        }
    }
}
