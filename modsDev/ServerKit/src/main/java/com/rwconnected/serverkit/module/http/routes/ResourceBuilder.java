package com.rwconnected.serverkit.module.http.routes;

import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ResourceBuilder {
    private final String basePath;
    private final Class<?> controllerClass;
    private final List<RouteDefinition> routes;
    private String[] methods = {"index", "show", "store", "update", "destroy"};

    public ResourceBuilder(String basePath, Class<?> controllerClass, List<RouteDefinition> routes) {
        this.basePath = basePath;
        this.controllerClass = controllerClass;
        this.routes = routes;
        registerAll();
    }

    public ResourceBuilder only(String... methods) {
        this.methods = methods;
        registerAll();
        return this;
    }

    public ResourceBuilder except(String... methodsToExclude) {
        this.methods = filterMethods(methodsToExclude);
        registerAll();
        return this;
    }

    private void registerAll() {
        routes.removeIf(r -> r.path.startsWith(basePath));
        for (String method : methods) {
            switch (method) {
                case "index" -> routes.add(new RouteDefinition("GET", basePath, controllerClass, "index"));
                case "show" -> routes.add(new RouteDefinition("GET", basePath + "/{id}", controllerClass, "show"));
                case "store" -> routes.add(new RouteDefinition("POST", basePath, controllerClass, "store"));
                case "update" -> routes.add(new RouteDefinition("PUT", basePath + "/{id}", controllerClass, "update"));
                case "destroy" -> routes.add(new RouteDefinition("DELETE", basePath + "/{id}", controllerClass, "destroy"));
            }
        }
    }

    private String[] filterMethods(String[] methodsToExclude) {
        return java.util.Arrays.stream(new String[]{"index", "show", "store", "update", "destroy"})
            .filter(method -> java.util.Arrays.stream(methodsToExclude).noneMatch(exclude -> exclude.equals(method)))
            .toArray(String[]::new);
    }
}
