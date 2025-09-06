package scripts.antiban;

import scripts.Logger;

import java.time.LocalDateTime;

public class runtimeTracker {
    private static Long startedAt = null;
    private static Logger logger = null;

    public static void initLogger(Logger log) {
        logger = log;
    }

    public static void init(){
        startedAt = System.currentTimeMillis(); //saves time in millis,that means 1,724,690,478,564 milliseconds have passed since Jan 1, 1970 UTC.
    }

    public static int hours(){ //returns elapsed hours
        int hours = (int) (calculateElapsedTime() / 3600000.0);
        if (logger != null) {
            logger.debug("[RuntimeTracker] - hours " + hours);
        }
        if (hours >= 24){
            init();
        }
        return hours;
    }

    public static int minutes() { //returns the minutes after subtracting the hours.
        int minutes = (int) Math.ceil((calculateElapsedTime() % 3600000) / 60000.0);
        //Math.ceil rounds up and returns double -> (int)
        //% gives the remainder after dividing by 3 600 000 which is 1 hour
        // 60 000 because we want it in minutes, not in millis
        if (logger != null) {
            logger.debug("[RuntimeTracker] - minutes: " + minutes);
        }
        return minutes;
    }

    public static int currentHour() { //returns current hour of the day, so 2:30 AM -> 2 and 11:59 PM -> 23
        int hour = LocalDateTime.now().getHour();
        if (logger != null) {
            logger.debug("[RuntimeTracker] - currentHour: " + hour);
        }
        return hour;
    }

    private static long calculateElapsedTime() { //calculates time since start of script
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
