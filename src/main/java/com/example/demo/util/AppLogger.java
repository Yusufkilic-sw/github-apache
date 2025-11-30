package com.example.demo.util;

import java.util.logging.Logger;

/**
 * Central application logger provider. Use AppLogger.LOGGER everywhere
 * to ensure a single logger instance across the application.
 */
public final class AppLogger {
    public static final Logger LOGGER = Logger.getLogger("com.example.demo");

    private AppLogger() {
        // utility
    }
}
