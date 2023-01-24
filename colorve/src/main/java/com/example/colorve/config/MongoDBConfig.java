package com.example.colorve.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {
 
    @Value("${spring.data.mongodb.uri}")
    private String connectionString;
 
    @Value("${spring.data.mongodb.database}")
    private String database;
 
    @Override
    protected String getDatabaseName() {
        return database;
    }
 
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(connectionString);
    }
}
