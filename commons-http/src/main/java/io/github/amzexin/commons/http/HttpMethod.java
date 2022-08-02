package io.github.amzexin.commons.http;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
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

    public boolean equals(HttpMethod httpMethod) {
        return this == httpMethod;
    }

}
