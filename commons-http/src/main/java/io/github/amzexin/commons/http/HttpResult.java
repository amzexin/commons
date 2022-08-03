package io.github.amzexin.commons.http;

public class HttpResult<T> {

    private Integer code = 500;

    private String message = "server error";

    private T data;

    public boolean successful() {
        return code == 200;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public HttpResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpResult() {
    }

    @Override
    public String toString() {
        return "HttpResult{" + "code=" + code + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}
