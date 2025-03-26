package scripts;

import org.tribot.script.sdk.Log;

public class Logger {
    private final String format;

    public Logger(String section) {
        this.format = section;
    }

    private String formatMessage(Object message) {
        return "[" + format + "] | " + message;
    }

    public void debug(String message) {
        Log.debug(formatMessage(message));
    }

    public void error(String message) {
        Log.error(formatMessage(message));
    }

    public void warn(String message) {
        Log.warn(formatMessage(message));
    }

    public void info(String message) {
        Log.info(formatMessage(message));
    }

    public void trace(String message) {
        Log.trace(formatMessage(message));
    }
}
