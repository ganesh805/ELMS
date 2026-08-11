package com.elms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * AppInfoLogger runs automatically after the Spring Application Context is loaded.
 * It reads the 'elms.app.environment' property from application.properties
 * and logs an startup message.
 */
@Component
public class AppInfoLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AppInfoLogger.class);

    // Injects property 'elms.app.environment' from application.properties
    @Value("${elms.app.environment:unknown}")
    private String environment;

    @Override
    public void run(String... args) throws Exception {
        log.info("=================================================");
        log.info("Starting ELMS in environment: {}", environment);
        log.info("=================================================");
    }
}
