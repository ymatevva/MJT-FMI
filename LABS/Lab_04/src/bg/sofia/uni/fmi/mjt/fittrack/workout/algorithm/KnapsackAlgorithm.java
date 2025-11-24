package bg.sofia.uni.fmi.mjt.fittrack.workout.algorithm;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnapsackAlgorithm implements AlgorithmAPI {

    private final List<Workout> availableWorkouts;

    public KnapsackAlgorithm(Collection<Workout> availableWorkouts) {
        this.availableWorkouts = new ArrayList<>(availableWorkouts);
    }

    private int[][] computeDP(int totalMinutes) throws OptimalPlanImpossibleException {

        int n = availableWorkouts.size();
        int[][] dp = new int[n + 1][totalMinutes + 1];

        for (int i = 1; i <= n; i++) {

            Workout w = availableWorkouts.get(i - 1);
            int time = w.getDuration();
            int cal = w.getCaloriesBurned();

            for (int t = 1; t <= totalMinutes; t++) {

                if (time > t) {
                    dp[i][t] = dp[i - 1][t];
                } else {
                    dp[i][t] = Math.max(
                            dp[i - 1][t],
                            dp[i - 1][t - time] + cal
                    );
                }
            }
        }

        return dp;
    }

    @Override
    public List<Workout> createOptimalPlan(int totalMinutes) throws OptimalPlanImpossibleException {

        int[][] dp = computeDP(totalMinutes);
        List<Workout> result = new ArrayList<>();

        int remainingTime = totalMinutes;

        for (int i = availableWorkouts.size(); i > 0 && remainingTime > 0; i--) {

            if (dp[i][remainingTime] != dp[i - 1][remainingTime]) {
                Workout w = availableWorkouts.get(i - 1);
                result.add(w);
                remainingTime -= w.getDuration();
            }
        }

        return result;
    }
}