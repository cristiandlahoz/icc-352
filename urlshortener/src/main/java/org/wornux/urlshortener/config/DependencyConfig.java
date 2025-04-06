package org.wornux.urlshortener.config;

import dev.morphia.Datastore;
import lombok.Getter;
import org.wornux.urlshortener.core.routing.DatastoreProvider;
import org.wornux.urlshortener.dao.*;
import org.wornux.urlshortener.service.*;

public class DependencyConfig {

    private static UserDAO userDAO;


    @Getter private static UserService userService;


    public static void init() {

        userDAO = new UserDAO(DatastoreProvider.getDatastore());
        userService = new UserService(userDAO);

    }
}
