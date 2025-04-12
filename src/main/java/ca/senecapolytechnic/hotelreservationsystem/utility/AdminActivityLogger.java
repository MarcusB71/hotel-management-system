package ca.senecapolytechnic.hotelreservationsystem.utility;

import java.io.IOException;
import java.util.logging.*;

public class AdminActivityLogger {
    private static final Logger logger = Logger.getLogger("AdminActivityLogger");

    static {
        try {
            FileHandler fileHandler = new FileHandler("admin_activity.%g.log", 1024 * 1024, 10, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console logging for admin activity
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Failed to set up AdminActivityLogger", e);
        }
    }

    public static void log(String activity) {
        logger.info(activity);
    }
}
