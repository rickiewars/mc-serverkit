package net.rickiewars.datetime.module.http.routes;

import java.util.List;

public class RouteDefinition {
    public String method;
    public String path;
    public Class<?> controllerClass;
    public String methodName;
    public List<String> pathSegments;
    boolean hasParams;

    public RouteDefinition(String method, String path, Class<?> controllerClass, String methodName) {
        this.method = method;
        this.path = path;
        this.controllerClass = controllerClass;
        this.methodName = methodName;
        this.pathSegments = List.of(path.split("/"));
        this.hasParams = path.contains("{");
    }
}
