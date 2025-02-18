package org.example.util;

public enum Routes {
    HOME("/"),
    LOGIN("/login"),
    SIGNUP("/signup"),
    CREATEARTICLE("/createarticle"),
    ARTICLES("/articles"),
    ARTICLE("/articles/{id}"),
    CREATEUSER("/createuser");

    private final String path;

    Routes(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}
