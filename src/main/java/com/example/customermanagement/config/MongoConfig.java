package com.example.customermanagement.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

/**
 * MongoDB configuration class that explicitly sets up the MongoDB connection
 * with hardcoded connection parameters instead of using properties.
 * This class overrides any MongoDB settings in application.properties.
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Hardcoded MongoDB connection settings
    private static final String MONGODB_HOST = "localhost"; // Replace with your actual MongoDB IP
    private static final int MONGODB_PORT = 27017;
    private static final String MONGODB_DATABASE = "customerdb";

    /**
     * Returns the database name to use.
     * @return the database name
     */
    @Override
    protected String getDatabaseName() {
        return MONGODB_DATABASE;
    }

    /**
     * Creates and configures a MongoClient with explicit server address.
     * This bean will override any connection settings from application.properties.
     *
     * @return a configured MongoClient instance
     */
    @Override
    @Bean
    @Primary
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(
                                new ServerAddress(MONGODB_HOST, MONGODB_PORT))))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    /**
     * Creates a MongoTemplate using the custom MongoClient.
     *
     * @return a configured MongoTemplate instance
     */
    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
