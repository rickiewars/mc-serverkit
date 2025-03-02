package com.rwconnected.serverkit.module.http;

import com.sun.net.httpserver.HttpExchange;
import com.rwconnected.serverkit.module.http.routes.RouteDefinition;
import com.rwconnected.serverkit.module.http.routes.RouteGroup;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Route {
    private static final List<RouteDefinition> routes = new ArrayList<>();

    public static void define(Consumer<RouteGroup> groupConsumer) {
        define("", groupConsumer);
    }
    public static void define(String prefix, Consumer<RouteGroup> groupConsumer) {
        RouteGroup rootGroup = new RouteGroup(prefix);
        groupConsumer.accept(rootGroup);
        routes.addAll(rootGroup.getRoutes());

        // Create one universal context for all requests
        HttpServerHandler.getServer().createContext("/", Route::routeHandler);
    }

    private static void routeHandler(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {
            for (RouteDefinition rd : routes) {
                if (matchPath(rd.path, requestPath) && rd.method.equalsIgnoreCase(method)) {
                    try {
                        Object controllerInstance = rd.controllerClass.getDeclaredConstructor().newInstance();
                        var targetMethod = getMethod(rd.controllerClass, rd.methodName);

                        Object[] args = getPathParams(rd.path, exchange, targetMethod);
                        targetMethod.invoke(controllerInstance, args);
                        return;
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("Method " + rd.methodName + " not found in " + rd.controllerClass.getName(), e);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("Failed to instantiate controller " + rd.controllerClass.getName(), e);
                    } catch (InvocationTargetException e) {
                        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                        if (e.getTargetException() instanceof NoSuchElementException) {
                            status = HttpStatus.NOT_FOUND;
                        }
                        JsonResponse.sendError(exchange, status, e.getTargetException().getMessage());
                        return;
                    }
                }
            }
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += ": " + e.getCause().getMessage();
            }
            JsonResponse.sendError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
            return;
        }

        JsonResponse.sendError(exchange, HttpStatus.NOT_FOUND, "Route + " + requestPath + " not found");
    }

    private static boolean matchPath(String routePath, String requestPath) {
        String[] routeSegments = routePath.split("/");
        String[] requestSegments = requestPath.split("/");

        if (routeSegments.length != requestSegments.length) return false;

        for (int i = 0; i < routeSegments.length; i++) {
            if (routeSegments[i].startsWith("{") && routeSegments[i].endsWith("}")) {
                // Parameter match
                continue;
            }
            if (!routeSegments[i].equals(requestSegments[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the path parameters from the request path defined like /path/{param1}/{param2}
     * @param path The path defined in the route
     * @param exchange The HttpExchange object
     * @param targetMethod The method to invoke
     * @return The path parameters
     */
    @NotNull
    private static Object[] getPathParams(String path, HttpExchange exchange, Method targetMethod) {
        String[] pathParams = extractPathParams(exchange.getRequestURI().getPath(), path);

        Object[] args = new Object[targetMethod.getParameterCount()];
        args[0] = exchange;
        for (int i = 1; i < args.length; i++) {
            args[i] = i - 1 < pathParams.length
                ? pathParams[i - 1]
                : null;
        }
        return args;
    }

    /**
     * Get the method from the controller class
     * @param controllerClass The controller class
     * @param methodName The method name
     * @return The method
     * @throws NoSuchMethodException If the method is not found
     */
    @NotNull
    private static Method getMethod(Class<?> controllerClass, String methodName) throws NoSuchMethodException {
        Method[] methods = controllerClass.getDeclaredMethods();
        Method targetMethod = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                targetMethod = m;
                break;
            }
        }

        if (targetMethod == null) {
            throw new NoSuchMethodException("Method " + methodName + " not found in " + controllerClass.getName());
        }
        return targetMethod;
    }

    private static String[] extractPathParams(String requestPath, String routePath) {
        // Extract path parameters dynamically
        String[] requestSegments = requestPath.split("/");
        String[] routeSegments = routePath.split("/");
        if (requestSegments.length != routeSegments.length) {
            return new String[0];
        }

        List<String> params = new ArrayList<>();
        for (int i = 0; i < routeSegments.length; i++) {
            if (routeSegments[i].startsWith("{") && routeSegments[i].endsWith("}")) {
                params.add(requestSegments[i]);
            } else if (!routeSegments[i].equals(requestSegments[i])) {
                params.add(requestSegments[i]);
            }
        }

        return params.toArray(new String[0]);
    }
}
