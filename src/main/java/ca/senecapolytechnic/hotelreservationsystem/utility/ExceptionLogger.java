package ca.senecapolytechnic.hotelreservationsystem.utility;

import java.io.IOException;
import java.util.logging.*;

public class ExceptionLogger {
    private static final Logger logger = Logger.getLogger("ExceptionLogger");

    static {
        try {
            // Log to console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);

            // Optional: log to file too
            FileHandler fileHandler = new FileHandler("exceptions.%g.log", 1024 * 1024, 5, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to set up ExceptionLogger: " + e.getMessage());
        }
    }

    public static void log(Level level, String message, Throwable throwable) {
        logger.log(level, message, throwable);
    }
}

