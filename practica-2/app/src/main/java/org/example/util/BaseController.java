package org.example.util;

import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;

public abstract class BaseController {

    protected Javalin app;

    public BaseController(Javalin app) {
        this.app = app;
    }

    abstract public void applyRoutes();

    public Map<String, Object> setModel(Object... args) {
        Map<String, Object> modelo = new HashMap<>();
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Invalid number of arguments. Arguments should be in pairs (key, value).");
        }
        for (int i = 0; i < args.length; i += 2) {
            if (!(args[i] instanceof String)) {
                throw new IllegalArgumentException(
                        "Argument at position " + i + " is not a String. Keys should be Strings.");
            }
            modelo.put((String) args[i], args[i + 1]);
        }
        return modelo;
    }

}
