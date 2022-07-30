package com.shade.dev.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShadeLogger {

    private final Logger logger;

    public ShadeLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    public void log(Level level, String log){
        logger.log(level, String.format(log));
    }
}
