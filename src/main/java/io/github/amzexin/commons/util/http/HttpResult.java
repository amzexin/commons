package io.github.amzexin.commons.util.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpResult<T> {

    private Integer code = 500;

    private String message = "server error";

    private T data;

    public boolean successful() {
        return code == 200;
    }

    public HttpResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
