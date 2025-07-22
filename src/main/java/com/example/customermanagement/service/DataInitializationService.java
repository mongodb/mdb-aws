package com.example.customermanagement.service;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Service to initialize the database with dummy customer data on application startup.
 * This service creates 5 sample customers if the database is empty.
 */
@Service
public class DataInitializationService {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    private final CustomerRepository customerRepository;

    /**
     * Constructor-based dependency injection for CustomerRepository
     *
     * @param customerRepository repository for Customer entities
     */
    @Autowired
    public DataInitializationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Initialize the database with dummy customer data.
     * This method runs after the service is constructed.
     * It checks if the database is empty before adding data to avoid duplication.
     */
    @PostConstruct
    public void initializeData() {
        logger.info("Checking if customer data initialization is needed...");

        if (customerRepository.count() == 0) {
            logger.info("Initializing database with dummy customer data");

            List<Customer> dummyCustomers = Arrays.asList(
                    new Customer("John Doe", "john.doe@example.com", "555-123-4567", "123 Main St, Anytown, USA"),
                    new Customer("Jane Smith", "jane.smith@example.com", "555-234-5678", "456 Oak Ave, Somewhere, USA"),
                    new Customer("Robert Johnson", "robert.johnson@example.com", "555-345-6789", "789 Pine Rd, Nowhere, USA"),
                    new Customer("Emily Davis", "emily.davis@example.com", "555-456-7890", "101 Maple Dr, Everywhere, USA"),
                    new Customer("Michael Wilson", "michael.wilson@example.com", "555-567-8901", "202 Cedar Ln, Anywhere, USA")
            );

            customerRepository.saveAll(dummyCustomers);
            logger.info("Successfully initialized database with {} customer records", dummyCustomers.size());
        } else {
            logger.info("Database already contains customer data. Skipping initialization.");
        }
    }
}
