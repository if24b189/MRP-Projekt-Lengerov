package org.example.server.http;

public enum ContentType {
    JSON("application/json; charset=utf-8"),
    TEXT("text/plain; charset=utf-8");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
