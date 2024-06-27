/**
 * A combination sum solver
 *
 * Given a list of distinct integers and an integer goal, print combinations of
 * integers that sum to the goal.
 */
public class ComboSum {

    private int goal;
    private int[] nums;

    private int[] state;

    public ComboSum(int goal, int... nums) {
        if (goal <= 0) {
            throw new IllegalArgumentException("goal must be > 0");
        }
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("invalid nums array");
        }
        this.goal = goal;
        this.nums = nums;
        this.state = new int[nums.length];
    }

    public void findSolutions() {
        findSolutions(0, 0);
    }

    private void findSolutions(int n, int total) {
        if (total == goal) {
            displaySolution(n);
            return;
        }

        if (n >= state.length) {
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (isValid(num, total)) {
                applyValue(num, n);
                findSolutions(n + 1, total + num);
                removeValue(n);
            }
        }
    }

    private void applyValue(int num, int stateIndex) {
        state[stateIndex] = num;
    }

    private void removeValue(int stateIndex) {
        state[stateIndex] = 0;
    }

    private boolean isValid(int num, int currentTotal) {
        return currentTotal + num <= goal;
    }

    public void displaySolution(int length) {
        System.out.print(state[0]);
        for (int i = 1; i < length; i++) {
            System.out.print(" + " + state[i]);
        }
        System.out.println(" = " + goal);
    }
}
