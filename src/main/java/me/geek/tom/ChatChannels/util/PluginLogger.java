package me.geek.tom.ChatChannels.util;

import java.util.logging.Logger;

public class PluginLogger {
    private Logger logger;
    private boolean debugEnabled;
    private String prefix;

    public PluginLogger(Logger logger, boolean debugEnabled, String prefix) {
        this.logger = logger;
        this.debugEnabled= debugEnabled;
        this.prefix = prefix;
    }

    public void info(String message) {
        logger.info(prefix + " " + message);
    }

    public void debug(String message) {
        if (debugEnabled) {
            logger.info(prefix + " [DEBUG] " + message);
        }
    }
}
