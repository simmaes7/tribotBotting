package scripts.antiban;

import scripts.Logger;
import java.time.LocalDateTime;

public class runtimeTracker {
    private static Long startedAt = null;
    private static Logger logger = null;

    // Private constructor to prevent instantiation.
    private runtimeTracker() { }

    /**
     * Initializes the logger for RuntimeTracker.
     * @param log the Logger instance to use.
     */
    public static void initLogger(Logger log) {
        logger = log;
    }

    /**
     * Initializes the runtime tracker by recording the current system time.
     */
    public static void init() {
        startedAt = System.currentTimeMillis();
    }

    /**
     * Returns the number of hours the script has been running.
     * If 24 or more hours have passed, it reinitializes the tracker.
     * @return the runtime in hours.
     */
    public static int hours() {
        int hrs = (int) (calculate() / 3600000.0);
        if (logger != null) {
            logger.debug("[RuntimeTracker] - hours: " + hrs);
        }
        if (hrs >= 24) {
            init();
        }
        return hrs;
    }

    /**
     * Returns the number of minutes (rounded up) within the current hour.
     * @return the minutes.
     */
    public static int minutes() {
        int mins = (int) Math.ceil((calculate() % 3600000) / 60000.0);
        if (logger != null) {
            logger.debug("[RuntimeTracker] - minutes: " + mins);
        }
        return mins;
    }

    /**
     * Returns the current hour of the day (0-23) using LocalDateTime.
     * @return the current hour.
     */
    public static int currentHour() {
        int currentHour = LocalDateTime.now().getHour();
        if (logger != null) {
            logger.debug("[RuntimeTracker] - currentHour: " + currentHour);
        }
        return currentHour;
    }

    /**
     * Calculates the elapsed time in milliseconds since the tracker was initialized.
     * If not initialized, it calls init() first.
     * @return the elapsed time in milliseconds.
     */
    private static long calculate() {
        if (startedAt == null) {
            init();
        }
        long elapsed = System.currentTimeMillis() - startedAt;
        if (logger != null) {
            logger.debug("[RuntimeTracker] - calculate: " + elapsed);
        }
        return elapsed;
    }
}
