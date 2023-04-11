namespace java iot.github.timeway.thrift.api.bean

struct HelloDTO {
    /**
     * 字符串类型
     */
    1: required string helloString,
    /**
     * 8位整形类型, 对应java中的byte类型
     */
    2: required i8 helloI8,
    /**
     * 16位整形类型, 对应java中的short类型
     */
    3: required i16 helloI16,
    /**
     * 32位整形类型, 对应C/C++/java中的int类型
     */
    4: required i32 helloI32,
    /**
     * 64位整形类型, 对应C/C++/java中的long类型
     */
    5: required i64 helloI64,
    /**
     * 8位的字符类型，对应C/C++中的char，java中的byte类型
     */
    6: required byte helloByte,
    /**
     * 布尔类型，对应C/C++中的bool，java中的boolean类型
     */
    7: required bool helloBool,
    /**
     * 双精度浮点类型，对应C/C++/java中的double类型
     */
    8: required double helloDouble,
    /**
     * map类型
     */
    9: required map<i32, string> helloMap,
    /**
     * set类型
     */
    10: required set<i32> helloSet,
    /**
     * list类型
     */
    11: required list<i32> helloList,
}