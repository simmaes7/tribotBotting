package scripts.antiban;

import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.antiban.AntibanProperties;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import scripts.Logger;

import java.util.Random;

public class fatigueResolver {
    // Capture current mouse speed at class load time.
    private static final int currentSpeed = Mouse.getSpeed();

    private static double minProp = 450.0;
    private static double maxProp = 1250.0;
    private static double sdProp = 90.0;
    private static Logger logger = null;

    private static final Random random = new Random();

    // Private constructor prevents instantiation.
    private fatigueResolver() { }

    /**
     * Simple Pair class to hold two values.
     */
    private static class Pair<F, S> {
        public final F first;
        public final S second;
        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * Retrieves the minimum and maximum mouse speeds based on player preferences.
     */
    private static Pair<Integer, Integer> getMinMaxMouseSpeeds() {
        int min = PlayerPreferences.preference(profilingPreferences.MIN_MOUSE_SPEED.key, g -> g.uniform(80, 110));
        int max = PlayerPreferences.preference(profilingPreferences.MAX_MOUSE_SPEED.key, g -> g.uniform(130, 160));
        return new Pair<>(min, max);
    }

    /**
     * Initializes the logger.
     */
    public static void initLogger(Logger log) {
        logger = log;
    }

    /**
     * Customizes the antiban properties.
     */
    public static void customize(Double min, Double max, Double sd) {
        if (min != null) {
            minProp = min;
        }
        if (max != null) {
            maxProp = max;
        }
        if (sd != null) {
            sdProp = sd;
        }
    }

    /**
     * Adjusts the mouse speed based on runtime and current hour.
     */
    private static void adjustMouseSpeed(Integer runtime, Integer currentHour) {
        if (runtime == null) {
            runtime = runtimeTracker.hours();
        }
        if (currentHour == null) {
            currentHour = runtimeTracker.currentHour();
        }
        double factor = getFactor(runtime, currentHour);
        int adjustedSpeed = (int) (currentSpeed / factor);
        Pair<Integer, Integer> speeds = getMinMaxMouseSpeeds();
        // Coerce adjustedSpeed between min and max.
        int coercedSpeed = Math.min(Math.max(adjustedSpeed, speeds.first), speeds.second);
        Mouse.setSpeed(coercedSpeed);
    }

    /**
     * Calculates a normally distributed delay using the Box–Muller transform.
     */
    private static Pair<Integer, Integer> calculateDelay(double mean, double sd) {
        double u1 = 1.0 - random.nextDouble();
        double u2 = 1.0 - random.nextDouble();
        double rndStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        double rndNormal = mean + sd * rndStdNormal;
        return new Pair<>((int) rndNormal, (int) rndStdNormal);
    }

    /**
     * Helper method to generate a random double between lower and upper.
     */
    private static double randomDouble(double lower, double upper) {
        return lower + (upper - lower) * random.nextDouble();
    }

    /**
     * Influences delays by adjusting the mean and standard deviation based on runtime and current hour.
     */
    public static int getMilliseconds(Integer paramRuntime, Integer paramCurrentHour, Double sd) {
        // Copy the parameters into new local variables
        final Integer runtime = (paramRuntime == null) ? runtimeTracker.hours() : paramRuntime;
        final Integer currentHour = (paramCurrentHour == null) ? runtimeTracker.currentHour() : paramCurrentHour;

        // sd is not final, but if you want it to be effectively final:
        if (sd == null) {
            sd = (maxProp - minProp) / 10.0;
        }

        double factor = getFactor(runtime, currentHour);
        double adjustedMean = ((minProp + maxProp) / 2.0) * factor;
        double adjustedSd = sd * factor;

        // Retrieve Antiban props
        AntibanProperties.Props props = AntibanProperties.getPropsForCurrentChar();
        props.setWaitingMaxModifier(maxProp);
        props.setWaitingMinModifier(minProp);
        props.setWaitingNormalDistStdModifier(adjustedSd);

        if (logger != null) {
            logger.warn("[Fatigue] - factor: " + factor
                    + " | mean: " + adjustedMean
                    + " | sd: " + adjustedSd
                    + " | calculatedDelay: " + calculateDelay(adjustedMean, adjustedSd).first);
        }

        // Now 'runtime' and 'currentHour' are effectively final, so we can use them in an anonymous class
        lottery.execute(randomDouble(0.05, 0.13), new Runnable() {
            @Override
            public void run() {
                adjustMouseSpeed(runtime, currentHour);
            }
        });

        return calculateDelay(adjustedMean, adjustedSd).first;
    }


    /**
     * Overloaded getMilliseconds methods.
     */
    public static int getMilliseconds(Integer runtime, Integer currentHour) {
        return getMilliseconds(runtime, currentHour, (maxProp - minProp) / 10.0);
    }

    public static int getMilliseconds() {
        return getMilliseconds(null, null, (maxProp - minProp) / 10.0);
    }

    /**
     * Helper method to wait for the calculated fatigue delay.
     */
    public static void await(int multiplier) {
        Waiting.wait(getMilliseconds() * multiplier);
    }

    public static void await() {
        await(1);
    }

    /**
     * Calculates the fatigue factor based on runtime and current hour.
     */
    private static double getFactor(int runtime, int currentHour) {
        double timeOfDayModifier;
        if (currentHour >= 4 && currentHour <= 9) {
            timeOfDayModifier = randomDouble(0.6, 0.85);
        } else if (currentHour >= 10 && currentHour <= 17) {
            timeOfDayModifier = randomDouble(0.95, 1.05);
        } else if (currentHour >= 18 && currentHour <= 23) {
            timeOfDayModifier = randomDouble(1.15, 1.3);
        } else {
            timeOfDayModifier = randomDouble(1.3, 1.45);
        }

        double runtimeFatigue;
        if (runtime < 3) {
            runtimeFatigue = randomDouble(0.85, 1.05);
        } else if (runtime < 6) {
            runtimeFatigue = randomDouble(1.05, 1.25);
        } else if (runtime < 10) {
            runtimeFatigue = randomDouble(1.25, 1.35);
        } else if (runtime < 16) {
            runtimeFatigue = randomDouble(1.35, 1.5);
        } else {
            runtimeFatigue = randomDouble(1.5, 1.6);
        }

        return timeOfDayModifier * runtimeFatigue;
    }
}
