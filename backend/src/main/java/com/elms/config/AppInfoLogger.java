package com.elms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Logs app environment on startup
@Component
public class AppInfoLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AppInfoLogger.class);

    // Read environment property from application.properties
    @Value("${elms.app.environment:unknown}")
    private String environment;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting ELMS in environment: {}", environment);
    }
}
