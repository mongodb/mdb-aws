package com.example.customermanagement.repository;

import com.example.customermanagement.model.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Customer entities.
 * Extends MongoRepository to inherit basic CRUD operations.
 * The interface works with Customer entities and uses ObjectId as the ID type.
 */
@Repository
public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {

    /**
     * Find a customer by email address
     *
     * @param email the email to search for
     * @return the customer with the specified email, or null if not found
     */
    Customer findByEmail(String email);

    /**
     * Find customers by name (containing the specified string, case-insensitive)
     *
     * @param name the name to search for
     * @return list of customers with names containing the specified string
     */
    java.util.List<Customer> findByNameContainingIgnoreCase(String name);

    /**
     * Check if a customer with the given email exists
     *
     * @param email the email to check
     * @return true if a customer with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
