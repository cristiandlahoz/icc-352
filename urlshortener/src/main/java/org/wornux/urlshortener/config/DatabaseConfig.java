package org.wornux.urlshortener.config;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConfig {
  private static Datastore datastore;

  public static Datastore getDatastore() {
    if (datastore == null) {
      String connectionString = System.getenv("MONGODB_URL");
      MongoClient mongoClient = MongoClients.create(connectionString);
      Morphia morphia = Morphia.createDatastore(mongoClient, "urlshortener");
      morphia.getMapper().mapPackage("org.wornux.urlshortener.models");
      morphia.ensureIndexes();
      datastore = morphia;
    }
    return datastore;
  }
}
