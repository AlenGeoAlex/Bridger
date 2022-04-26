package io.github.alenalex.bridger.utils;

import java.util.logging.Logger;

public class Debug {

    private static final Logger DEBUG_LOGGER;
    private static boolean DEBUG_MODE;

    static {
        DEBUG_LOGGER = Logger.getLogger("BRIDGER-DEBUG");
        DEBUG_MODE = false;
    }

    public static void setDebugMode(boolean debugMode) {
        DEBUG_MODE = debugMode;
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    public static void log(String message) {
        if (DEBUG_MODE) {
            DEBUG_LOGGER.info(message);
        }
    }

    public static void error(String message) {
        if (DEBUG_MODE) {
            DEBUG_LOGGER.severe(message);
        }
    }

    public static void warn(String message) {
        if (DEBUG_MODE) {
            DEBUG_LOGGER.warning(message);
        }
    }
}
