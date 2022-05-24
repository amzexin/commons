package com.amzexin.util.http;

public enum HttpMethod {
    /**
     * GET
     */
    GET("get"),
    /**
     * POST
     */
    POST("post"),
    /**
     * PUT
     */
    PUT("put"),
    /**
     * DELETE
     */
    DELETE("delete");

    /**
     * VALUE
     */
    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public boolean equals(HttpMethod httpMethod) {
        return this == httpMethod;
    }

    public String getValue() {
        return value;
    }
}
