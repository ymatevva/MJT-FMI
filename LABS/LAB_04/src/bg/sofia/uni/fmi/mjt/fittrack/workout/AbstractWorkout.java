package bg.sofia.uni.fmi.mjt.fittrack.workout;

import bg.sofia.uni.fmi.mjt.fittrack.exception.InvalidWorkoutException;

public abstract sealed class AbstractWorkout implements Workout permits StrengthWorkout, YogaSession, CardioWorkout {
    protected final String name;
    protected final int duration;
    protected final int caloriesBurned;
    protected final int difficulty;

    public AbstractWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        validateData(name, duration, caloriesBurned, difficulty);
        this.name = name;
        this.difficulty = difficulty;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    protected void validateData(String name, int duration, int caloriesBurned, int difficulty) {
        if (name == null || name.isBlank()) {
            throw new InvalidWorkoutException("The name should not be null.");
        }

        if (duration <= 0) {
            throw new InvalidWorkoutException("The duration should be a positive number.");
        }

        if (caloriesBurned <= 0) {
            throw new InvalidWorkoutException("The calories burned should be a positive number.");
        }

        if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
            throw new InvalidWorkoutException("The difficulty should be a positive number.");
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public int getCaloriesBurned() {
        return this.caloriesBurned;
    }

    @Override
    public int getDifficulty() {
        return this.difficulty;
    }

    @Override
    public abstract WorkoutType getType();
}
