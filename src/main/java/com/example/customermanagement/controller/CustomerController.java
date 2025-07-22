package com.example.customermanagement.controller;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.repository.CustomerRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for handling Customer-related HTTP requests.
 * Provides endpoints for CRUD operations on Customer entities.
 */
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*") // Allow requests from any origin for development
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;

    /**
     * Constructor-based dependency injection for CustomerRepository
     *
     * @param customerRepository repository for Customer entities
     */
    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Get all customers
     *
     * @return list of all customers
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        logger.debug("Getting all customers");
        return customerRepository.findAll();
    }

    /**
     * Get a customer by ID
     *
     * @param id the customer ID as a string
     * @return the customer if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id) {
        logger.debug("Getting customer with ID: {}", id);

        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Customer> customerData = customerRepository.findById(objectId);

            if (customerData.isPresent()) {
                return new ResponseEntity<>(customerData.get(), HttpStatus.OK);
            } else {
                logger.warn("Customer with ID {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ObjectId format: {}", id, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create a new customer
     *
     * @param customer the customer to create
     * @return the created customer with 201 Created status
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        logger.debug("Creating new customer: {}", customer);

        try {
            // Ensure ID is null for new customers
            customer.setId(null);
            Customer newCustomer = customerRepository.save(customer);
            logger.info("Customer created with ID: {}", newCustomer.getStringId());
            return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating customer", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing customer
     *
     * @param id the ID of the customer to update as a string
     * @param customer the updated customer data
     * @return the updated customer if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String id, @RequestBody Customer customer) {
        logger.debug("Updating customer with ID: {}", id);

        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Customer> customerData = customerRepository.findById(objectId);

            if (customerData.isPresent()) {
                Customer existingCustomer = customerData.get();
                existingCustomer.setName(customer.getName());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setPhone(customer.getPhone());
                existingCustomer.setAddress(customer.getAddress());

                Customer updatedCustomer = customerRepository.save(existingCustomer);
                logger.info("Customer updated: {}", updatedCustomer.getStringId());
                return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
            } else {
                logger.warn("Customer with ID {} not found for update", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ObjectId format for update: {}", id, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error updating customer with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a customer by ID
     *
     * @param id the ID of the customer to delete as a string
     * @return 204 No Content if successful, 404 Not Found if customer doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") String id) {
        logger.debug("Deleting customer with ID: {}", id);

        try {
            ObjectId objectId = new ObjectId(id);
            Optional<Customer> customer = customerRepository.findById(objectId);

            if (customer.isPresent()) {
                customerRepository.deleteById(objectId);
                logger.info("Customer deleted: {}", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.warn("Customer with ID {} not found for deletion", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ObjectId format for deletion: {}", id, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error deleting customer with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete all customers
     *
     * @return 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllCustomers() {
        logger.debug("Deleting all customers");

        try {
            customerRepository.deleteAll();
            logger.info("All customers deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting all customers", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
