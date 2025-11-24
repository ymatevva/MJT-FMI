import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TaskDistributor {

    public static final int ZERO_TASKS = 0;
    public static final int ONE_TASK = 1;
    public static final int TWO_TASKS = 2;

    public static int minDifference(int[] tasks) {

        if (tasks.length == ZERO_TASKS) {
            return 0;
        }

        if (tasks.length == ONE_TASK) {
            return tasks[0];
        }

        if (tasks.length == TWO_TASKS) {
            return Math.abs(tasks[0] - tasks[1]);
        }

        Arrays.sort(tasks);
        int firstGroupTime = tasks[tasks.length - 2];
        int secondGroupTime = tasks[tasks.length - 1];
        int minDifference = Math.abs(secondGroupTime - firstGroupTime);

        for (int i = tasks.length - 3; i >= 0; i--) {
            if (Math.abs(firstGroupTime + tasks[i] - secondGroupTime) <= minDifference ||
                    Math.abs(secondGroupTime + tasks[i] - firstGroupTime) > minDifference) {
                firstGroupTime += tasks[i];
                minDifference = Math.abs(firstGroupTime - secondGroupTime);
            } else {
                secondGroupTime += tasks[i];
            }
        }

        return Math.abs(secondGroupTime - firstGroupTime);
    }


    static void main() {
        System.out.println(minDifference(new int[]{1, 2, 3, 4, 5}));
        System.out.println(minDifference(new int[]{10, 20, 15, 5}));
        System.out.println(minDifference(new int[]{7, 3, 2, 1, 5, 4}));
        System.out.println(minDifference(new int[]{9, 1, 1, 1}));
        System.out.println(minDifference(new int[]{}));
        System.out.println(minDifference(new int[]{120}));
        System.out.println(minDifference(new int[]{30, 30}));
    }

}
