package net.rickiewars.datetime.module.http.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class RouteGroup {
    private final String prefix;
    private final List<RouteDefinition> routes = new ArrayList<>();

    public RouteGroup(String prefix) {
        this.prefix = prefix;
    }

    public void get(String path, Class<?> controllerClass, String methodName) {
        routes.add(new RouteDefinition("GET", prefix + path, controllerClass, methodName));
    }

    public void post(String path, Class<?> controllerClass, String methodName) {
        routes.add(new RouteDefinition("POST", prefix + path, controllerClass, methodName));
    }

    public void put(String path, Class<?> controllerClass, String methodName) {
        routes.add(new RouteDefinition("PUT", prefix + path, controllerClass, methodName));
    }

    public void delete(String path, Class<?> controllerClass, String methodName) {
        routes.add(new RouteDefinition("DELETE", prefix + path, controllerClass, methodName));
    }

    public ResourceBuilder apiResource(String basePath, Class<?> controllerClass) {
        return new ResourceBuilder(prefix + basePath, controllerClass, routes);
    }

    public List<RouteDefinition> getRoutes() {
        return routes;
    }

    public void group(String subPrefix, Consumer<RouteGroup> groupConsumer) {
        RouteGroup subGroup = new RouteGroup(prefix + subPrefix);
        groupConsumer.accept(subGroup);
        routes.addAll(subGroup.getRoutes());
    }
}
