package me.inver.app;

import org.apache.logging.log4j.LogManager;

public class Main {
    public static void main(String[] args) {
        var logger = LogManager.getRootLogger();
        logger.info("TEST");
    }
}
