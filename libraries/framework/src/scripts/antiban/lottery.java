package scripts.antiban;

import scripts.Logger;

import java.util.Random;

/**
 * Utility class to randomize certain actions on a probability(%) basis.
 */
public class lottery {
    private static Logger logger = null;
    private static Random rand = new Random();

    public static void initLogger(Logger log) {
        logger = log;
    }

    /**
     * Pass through a probability and Runnable to execute if the roll is within the probability threshold.
     *
     * @param probability A value between 0.0 and 1.0 (e.g., 0.15 = 15% chance)
     * @param action      The action to execute if “won”
     */
    public static void execute(double probability, Runnable action) {
        boolean won = shouldExecute(probability);

        if (won) {
            if (logger != null) {
                logger.info("[Lottery/Antiban] - Executing randomized action");
            }
            action.run();
        }
    }

    /**
     * Probability refers to the rough percentage % this action will be executed on.
     *
     * @param probability A value between 0.0 and 1.0 (inclusive)
     * @return true if the randomized roll is under the probability threshold
     */
    private static boolean shouldExecute(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }

        // Random double in [0.0, 1.0)
        double diceRoll = rand.nextDouble();
        return diceRoll < probability;
    }
}
