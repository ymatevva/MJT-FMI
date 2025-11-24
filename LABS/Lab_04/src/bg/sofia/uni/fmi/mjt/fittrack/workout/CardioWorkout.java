package bg.sofia.uni.fmi.mjt.fittrack.workout;

import java.util.Objects;

public final class CardioWorkout extends AbstractWorkout {

    public CardioWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        super(name, duration, caloriesBurned, difficulty);
    }

    @Override
    public String toString() {
        return "CardioWorkout{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", caloriesBurned=" + caloriesBurned +
                ", difficulty=" + difficulty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CardioWorkout that)) {
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
        return WorkoutType.CARDIO;
    }
}
