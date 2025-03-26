package scripts.antiban;

import scripts.Logger;
import java.util.Random;

public class lottery {
    private static Logger logger = null;
    private static final Random random = new Random();

    // Private constructor to prevent instantiation.
    private lottery() { }

    /**
     * Initializes the logger for Lottery.
     * @param log The Logger instance.
     */
    public static void initLogger(Logger log) {
        logger = log;
    }

    /**
     * Executes the provided action if a randomly generated value is below the given probability.
     * @param probability A double value between 0.0 and 1.0 representing the probability.
     * @param action A Runnable representing the action to execute.
     */
    public static void execute(double probability, Runnable action) {
        if (shouldExecute(probability)) {
            if (logger != null) {
                logger.info("[Lottery/Antiban] - Executing randomized action");
            }
            action.run();
        }
    }

    /**
     * Determines whether the action should be executed based on the given probability.
     * @param probability A double value between 0.0 and 1.0.
     * @return true if a randomly generated value is less than the probability.
     * @throws IllegalArgumentException if the probability is not between 0.0 and 1.0.
     */
    private static boolean shouldExecute(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }
        double diceRoll = random.nextDouble(); // Random value between 0.0 and 1.0.
        return diceRoll < probability;
    }
}
