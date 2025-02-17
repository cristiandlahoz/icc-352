package org.example.util;

public enum Routes {
    HOME("/"),
    LOGIN("/login"),
    ARTICLES("/articles"),
    ARTICLE("/articles/{id}");

    private final String path;

    Routes(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}
