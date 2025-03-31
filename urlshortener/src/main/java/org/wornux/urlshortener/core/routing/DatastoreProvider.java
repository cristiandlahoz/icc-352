package org.wornux.urlshortener.core.routing;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

import org.wornux.urlshortener.enums.DatastoreConstants;
import com.mongodb.client.MongoClients;

public class DatastoreProvider {
  private static Datastore datastore;

  public static Datastore getDatastore() {
    if (datastore == null) {
      String connectionString = System.getenv("MONGODB_URL");
      datastore = Morphia.createDatastore(MongoClients.create(connectionString),
          DatastoreConstants.MONGO_DATABASE_NAME.getValue());
      datastore.getMapper().mapPackage("org.wornux.urlshortener.model");
      datastore.ensureIndexes();
    }
    return datastore;
  }
}
