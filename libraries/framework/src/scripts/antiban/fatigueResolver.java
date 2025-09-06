package scripts.antiban;

import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.antiban.AntibanProperties;
import scripts.Logger;

import java.util.Random;

public class fatigueResolver {
    private static final int currentSpeed = Mouse.getSpeed();

    private static double minProp = 450.0;
    private static double maxProp = 1250.0;
    private static double sdProp = 90.0;
    private static Logger logger = null;
    private static final Random random = new Random();

    private static int[] getMinMaxMouseSpeeds(){
        int min = 80 + random.nextInt(110 - 80 + 1);
        int max = 130 + random.nextInt(160 - 130 + 1);
        return new int[]{min, max};
    }

    public static void initLogger(Logger log) {logger = log;}

    public static void customize(Double min, Double max, Double sd){
        if (min != null){minProp = min;}
        if (max != null){maxProp = max;}
        if (sd != null){sdProp = sd;}
    }

    private static void adjustMouseSpeed(Integer runtime, Integer currentHour) {
        int runtimeValue = (runtime == null) ? runtimeTracker.hours() : runtime;
        int currentHourValue = (currentHour == null) ? runtimeTracker.currentHour() : currentHour;

        double factor = getFactor(runtimeValue, currentHourValue);

        int[] minMax = getMinMaxMouseSpeeds();
        int coercedInSpeed = (int) (currentSpeed / factor);
        coercedInSpeed = Math.max(minMax[0], Math.min(coercedInSpeed, minMax[1]));

        Mouse.setSpeed(coercedInSpeed);
    }

    private static int[] calculateDelay(double mean, double sd) {
        double u1 = 1.0 - random.nextDouble();
        double u2 = 1.0 - random.nextDouble();

        double rndStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        double rndNormal = mean + sd * rndStdNormal;

        return new int[]{(int) rndNormal, (int) rndStdNormal};
    }

    /**
     * Influences the provided delays by adjusting the mean and standard deviation,
     * based on both the current time of day and how long the bot has been running
     */
    public static int getMilliseconds(Integer runtime, Integer currentHour, Double sd) {
        int runtimeValue = (runtime == null) ? runtimeTracker.hours() : runtime;
        int currentHourValue = (currentHour == null) ? runtimeTracker.currentHour() : currentHour;

        double factor = getFactor(runtimeValue, currentHourValue);
        double adjustedMean = ((minProp + maxProp) / 2.0) * factor;
        double adjustedSd = (sd != null ? sd : (maxProp - minProp) / 10.0) * factor;

        AntibanProperties.Props props = new AntibanProperties.Props();
        props.setWaitingMaxModifier(maxProp);
        props.setWaitingMinModifier(minProp);
        props.setWaitingNormalDistStdModifier(adjustedSd);

        int calculatedDelay = calculateDelay(adjustedMean, adjustedSd)[0];

        if (logger != null) {
            logger.warn("[Fatigue] - factor: " + factor
                    + " | mean: " + adjustedMean
                    + " | sd: " + adjustedSd
                    + " | calculatedDelay: " + calculatedDelay);
        }

        // Use Lottery utility: random probability between 0.05 and 0.13
        double probability = 0.05 + (0.13 - 0.05) * random.nextDouble();
        lottery.execute(probability, () -> adjustMouseSpeed(runtimeValue, currentHourValue));

        return calculatedDelay;
    }

    public static int getMilliseconds() {
        return getMilliseconds(null, null, null);
    }

    /**
     * Slight helper method to call
     * - FatigueResolver.await()
     * instead of
     * - Waiting.wait(FatigueResolver.getMilliseconds())
     */
    public static void await(int multiplier) {
        Waiting.wait(getMilliseconds() * multiplier);
    }

    public static void await() {
        await(1);
    }

    /**
     * Calculates the Fatigue factor in play, based off the time of day and the time running the bot.
     */
    private static double getFactor(int runtime, int currentHour) {
        double timeOfDayModifier;
        if (currentHour >= 4 && currentHour <= 9) {
            timeOfDayModifier = 0.6 + (0.85 - 0.6) * random.nextDouble(); // Early morning - focused
        } else if (currentHour >= 10 && currentHour <= 17) {
            timeOfDayModifier = 0.95 + (1.05 - 0.95) * random.nextDouble(); // Daytime - standard
        } else if (currentHour >= 18 && currentHour <= 23) {
            timeOfDayModifier = 1.15 + (1.3 - 1.15) * random.nextDouble(); // Early evening - getting tired
        } else {
            timeOfDayModifier = 1.3 + (1.45 - 1.3) * random.nextDouble(); // Night - tired
        }

        double runtimeFatigue;
        if (runtime < 3) {
            runtimeFatigue = 0.85 + (1.05 - 0.85) * random.nextDouble(); // First three hours - fully alert
        } else if (runtime < 6) {
            runtimeFatigue = 1.05 + (1.25 - 1.05) * random.nextDouble(); // 3 to 6 hours - normal
        } else if (runtime < 10) {
            runtimeFatigue = 1.25 + (1.35 - 1.25) * random.nextDouble(); // 6 to 10 hours - getting tired
        } else if (runtime < 16) {
            runtimeFatigue = 1.35 + (1.5 - 1.35) * random.nextDouble(); // 10 to 16 hours - a bit more tired
        } else {
            runtimeFatigue = 1.5 + (1.6 - 1.5) * random.nextDouble(); // After 16 hours - tired
        }

        return timeOfDayModifier * runtimeFatigue;
    }
}
