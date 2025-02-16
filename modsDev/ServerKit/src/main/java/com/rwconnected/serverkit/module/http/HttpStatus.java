package com.rwconnected.serverkit.module.http;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    ALREADY_REPORTED(208, "Already Reported"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");



    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public boolean sendResponseHeaders(HttpExchange exchange, int code, String message) {
        try {
            exchange.sendResponseHeaders(code, message.length());
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }
}
