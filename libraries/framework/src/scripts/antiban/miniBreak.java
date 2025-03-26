package scripts.antiban;

import lombok.Getter;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.util.TribotRandom;
import scripts.Logger;

public class miniBreak {
    /**
     * -- GETTER --
     *  Returns whether a mini break is currently active.
     */
    @Getter
    private static boolean active = false;

    // Private constructor to prevent instantiation
    private miniBreak() {}

    /**
     * Leaves the screen for a randomized duration based on the given mean and standard deviation.
     * Default: mean = 15000ms, sd = 2000ms.
     */
    public static void leave() {
        leave(15000, 2000);
    }

    public static void leave(int mean, int sd) {
        int milliseconds = TribotRandom.normal(mean, sd);
        active = true;

        // Log the action
        Logger logger = new Logger("MiniBreak");
        logger.info("Leaving screen for " + milliseconds + "ms");

        // Move the mouse off-screen and wait
        Mouse.leaveScreen();
        Waiting.wait(milliseconds);

        active = false;
    }

    public static void fatigueLeave(int multiplier) {
        int milliseconds = fatigueResolver.getMilliseconds() * multiplier;
        active = true;

        Logger logger = new Logger("MiniBreak");
        logger.info("Leaving screen for " + milliseconds + "ms (fatigue-based)");

        Mouse.leaveScreen();
        Waiting.wait(milliseconds);

        active = false;
    }

    public static void pause(int mean, int sd) {
        active = true;

        int milliseconds = TribotRandom.normal(mean, sd);
        Waiting.wait(milliseconds);

        active = false;
    }

    public static void fatiguePause(int multiplier) {
        active = true;

        int milliseconds = fatigueResolver.getMilliseconds() * multiplier;
        Waiting.wait(milliseconds);

        active = false;
    }

}
