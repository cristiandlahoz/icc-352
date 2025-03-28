package org.wornux.urlshortener.config;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseConfig {
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            String uri = System.getenv("MONGODB_URL");
            mongoClient = MongoClients.create(uri);
        }
        return mongoClient.getDatabase("urlshortener");
    }
}
