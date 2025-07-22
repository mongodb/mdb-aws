package com.example.customermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.PreDestroy;

/**
 * Main application class for the Customer Management application.
 * This class serves as the entry point for the Spring Boot application.
 * Includes shutdown hooks to ensure proper resource cleanup on application termination.
 */
@SpringBootApplication
public class CustomerManagementApplication implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerManagementApplication.class);
    private static ConfigurableApplicationContext applicationContext;

    /**
     * Main method that launches the Spring Boot application.
     * Sets up additional shutdown hooks for graceful termination.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting Customer Management Application");

        // Store application context for shutdown hook
        applicationContext = SpringApplication.run(CustomerManagementApplication.class, args);

        // Register JVM shutdown hook as a backup for non-Spring initiated shutdowns
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("JVM shutdown hook triggered - initiating graceful shutdown");

            if (applicationContext != null && applicationContext.isActive()) {
                try {
                    // Close the Spring context if it's still active
                    applicationContext.close();
                    logger.info("Application context closed successfully from JVM shutdown hook");
                } catch (Exception e) {
                    logger.error("Error during application shutdown from JVM hook: {}", e.getMessage(), e);
                }
            }

            logger.info("JVM shutdown hook completed");
        }, "shutdown-hook"));

        logger.info("Customer Management Application started successfully");
    }

    /**
     * Spring context shutdown event listener.
     * This is called when the Spring context is being closed.
     *
     * @param event the context closed event
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Application context is closing - performing final cleanup tasks");
        performApplicationCleanup();
    }

    /**
     * Method called when the bean is being destroyed.
     * Ensures resources are released properly.
     */
    @PreDestroy
    public void onDestroy() {
        logger.info("Application bean is being destroyed - releasing resources");
        performApplicationCleanup();
    }

    /**
     * Performs cleanup operations when the application is shutting down.
     * This includes ensuring all connections are closed and resources are released.
     */
    private void performApplicationCleanup() {
        logger.info("Performing application cleanup");

        try {
            // Additional cleanup logic can be added here if needed
            // Most resources should be cleaned up by their respective @PreDestroy methods

            logger.info("Application cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Error during application cleanup: {}", e.getMessage(), e);
        }
    }
}
