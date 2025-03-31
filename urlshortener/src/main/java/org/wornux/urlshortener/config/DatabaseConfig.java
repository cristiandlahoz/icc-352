package org.wornux.urlshortener.config;

import com.mongodb.client.MongoClients;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DatabaseConfig {
  private static Datastore datastore;

  public static Datastore getDatastore() {
    if (datastore == null) {
      String connectionString = System.getenv("MONGODB_URL");
      datastore = Morphia.createDatastore(MongoClients.create(connectionString), "urlshortener");
      datastore.getMapper().mapPackage("org.wornux.urlshortener.model");
      datastore.ensureIndexes();
    }
    return datastore;
  }
}
