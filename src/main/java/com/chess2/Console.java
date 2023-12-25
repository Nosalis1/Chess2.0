package com.chess2;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Console {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;

    private static int logLevel = DEBUG;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void log(int level, String message) {
        if (level >= logLevel) {
            StackTraceElement caller = getCaller();
            String timestamp = getTimestamp();
            String coloredPrefix = getColoredPrefix(level);
            String logEntry = String.format("%s %s %s[%s.%s()] %s%s",
                    timestamp, coloredPrefix, ANSI_RESET,
                    caller.getClassName(), caller.getMethodName(),
                    message, ANSI_RESET);
            outputLog(logEntry);
        }
    }

    @SuppressWarnings("unused")
    public static void setLogLevel(int level) {
        logLevel = level;
    }

    private static String getTimestamp() {
        return dateFormat.format(new Date());
    }

    private static String getColoredPrefix(int level) {
        return switch (level) {
            case DEBUG -> ANSI_BLUE + "[DEBUG]";
            case INFO -> ANSI_CYAN + "[INFO] ";
            case WARNING -> ANSI_YELLOW + "[WARN] ";
            case ERROR -> ANSI_RED + "[ERROR]";
            default -> "";
        };
    }

    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // Index 0 is getStackTrace(), index 1 is this method, index 2 is the caller
        return stackTrace[2];
    }

    private static void outputLog(String logEntry) {
        System.out.println(logEntry);
    }
}
