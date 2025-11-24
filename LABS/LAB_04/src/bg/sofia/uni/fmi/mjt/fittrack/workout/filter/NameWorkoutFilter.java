package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class NameWorkoutFilter implements WorkoutFilter {
    private final String keyword;
    private final boolean caseSensitive;

    public NameWorkoutFilter(String keyword, boolean caseSensitive) {
        validateData(keyword);
        this.keyword = keyword;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(Workout workout) {
        String workoutName = workout.getName();

        for (int i = 0; i <= workoutName.length() - keyword.length(); i++) {
            if (!caseSensitive) {
                if (workoutName.substring(i, i + keyword.length()).toLowerCase().equals(keyword.toLowerCase())) {
                    return true;
                }
            } else {
                if (workoutName.substring(i, i + keyword.length()).equals(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void validateData(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("The keyword cannot be null.");
        }

        if (keyword.isEmpty()) {
            throw new IllegalArgumentException("The keyword cannot be an empty string.");
        }
    }
}
