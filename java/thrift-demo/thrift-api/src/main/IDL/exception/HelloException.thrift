namespace java iot.github.timeway.thrift.api.exception

exception HelloException{
    /**
     * 异常码
     */
    1: required i32 code,
    /**
     * 异常内容
     */
    2: required string message,
}