package net.rickiewars.datetime.module.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import net.rickiewars.datetime.DateTime;

import java.io.IOException;
import java.io.OutputStream;

public class JsonResponse {
    private static final Gson gson = new Gson();

    public static void send(HttpExchange exchange, HttpStatus statusCode, Object data) {
        JsonResponse.send(exchange, statusCode, gson.toJsonTree(data));
    }

    public static void send(HttpExchange exchange, HttpStatus statusCode, JsonElement data) {
        JsonObject response = new JsonObject();
        response.addProperty("success", statusCode.isSuccess());
        response.add("data", data);
        sendResponse(exchange, statusCode, response);
    }

    public static void sendError(HttpExchange exchange, HttpStatus statusCode, String errorMessage) {
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("error", statusCode.getMessage() + ": " + errorMessage);
        sendResponse(exchange, statusCode, response);
    }

    private static void sendResponse(HttpExchange exchange, HttpStatus statusCode, JsonObject response) {
        String jsonResponse = gson.toJson(response);
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode.code(), jsonResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
        } catch (IOException e) {
            DateTime.LOGGER.error("HttpServer error: Failed to send JSON response", e);
        }
    }
}
