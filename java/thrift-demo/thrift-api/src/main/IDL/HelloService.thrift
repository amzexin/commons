// 定义Service的包路径，对应java的package
namespace java iot.github.timeway.thrift.api.service

// 引入Service的依赖
include "bean/HelloDTO.thrift"
include "exception/HelloException.thrift"

service HelloService {

    /**
     * hello
     */
    HelloDTO.HelloDTO hello(1:string str) throws (1:HelloException.HelloException helloException),

    /**
     * helloVoid
     * void，空类型，对应C/C++/java中的void类型；该类型主要用作函数的返回值，
     */
    void helloVoid(1:string str) throws (1:HelloException.HelloException helloException),
}