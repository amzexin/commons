package io.github.amzexin.springcloudstudy.passport.api.dto;

/**
 * 微服务通用返回结果
 * TODO HTTP接口监控的工具待导入
 */
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;

    public static CommonResult<Object> success() {
        CommonResult<Object> result = new CommonResult<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static CommonResult<Object> failed(Exception e) {
        CommonResult<Object> result = new CommonResult<>();
        result.setCode(500);
        result.setMessage("success exception: " + e.getMessage());
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
