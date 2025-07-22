package com.example.customermanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Customer entity class that maps to the MongoDB collection.
 * Contains customer information such as name, email, phone, and address.
 */
@Document(collection = "customers")
public class Customer {

    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * Default constructor
     */
    public Customer() {
    }

    /**
     * Parameterized constructor with all fields except id
     *
     * @param name    customer name
     * @param email   customer email
     * @param phone   customer phone number
     * @param address customer address
     */
    public Customer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Fully parameterized constructor
     *
     * @param id      MongoDB ObjectId
     * @param name    customer name
     * @param email   customer email
     * @param phone   customer phone number
     * @param address customer address
     */
    public Customer(ObjectId id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Get the customer id as ObjectId
     *
     * @return the customer id
     */
    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    /**
     * Get the customer id as String for frontend compatibility
     *
     * @return the customer id as String
     */
    @JsonProperty("id")
    public String getStringId() {
        return id != null ? id.toHexString() : null;
    }

    /**
     * Set the customer id from ObjectId
     *
     * @param id the customer id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Set the customer id from String
     *
     * @param id the customer id as String
     */
    public void setStringId(String id) {
        if (id != null && ObjectId.isValid(id)) {
            this.id = new ObjectId(id);
        }
    }

    /**
     * Get the customer name
     *
     * @return the customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the customer name
     *
     * @param name the customer name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the customer email
     *
     * @return the customer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the customer email
     *
     * @param email the customer email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the customer phone number
     *
     * @return the customer phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the customer phone number
     *
     * @param phone the customer phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get the customer address
     *
     * @return the customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the customer address
     *
     * @param address the customer address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the Customer object
     *
     * @return string representation of Customer
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
