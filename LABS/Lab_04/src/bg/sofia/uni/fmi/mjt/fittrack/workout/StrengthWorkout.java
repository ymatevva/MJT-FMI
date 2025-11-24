package bg.sofia.uni.fmi.mjt.fittrack.workout;

import java.util.Objects;

public final class StrengthWorkout extends AbstractWorkout {

    public StrengthWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public String toString() {
        return "StrengthWorkout{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", difficulty=" + difficulty +
                ", caloriesBurned=" + caloriesBurned +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StrengthWorkout that)) {
            return false;
        }

        return duration == that.duration &&
                caloriesBurned == that.caloriesBurned &&
                difficulty == that.difficulty &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public WorkoutType getType() {
        return WorkoutType.STRENGTH;
    }
}
