package com.parker.stack.constants;

public enum LocalStackConstants {
    API_GATEWAY("api-gateway");

    private final String serviceName;

    LocalStackConstants(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getValue() {
        return serviceName;
    }

    @Override
    public String toString() {
        return serviceName;
    }
}
