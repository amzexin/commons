package iot.github.timeway.thrift.server.service;

import iot.github.timeway.thrift.api.bean.HelloDTO;
import iot.github.timeway.thrift.api.exception.HelloException;
import iot.github.timeway.thrift.api.service.HelloService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * HelloServiceImpl
 *
 * @author Zexin Li
 * @date 2023-04-11 17:25
 */
public class HelloServiceImpl implements HelloService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public HelloDTO hello(String str) throws HelloException, TException {
        logger.info("hello: {}", str);
        HelloDTO helloDTO = new HelloDTO();
        helloDTO.setHelloString("hello");
        helloDTO.setHelloI8(Byte.MAX_VALUE);
        helloDTO.setHelloI16(Short.MAX_VALUE);
        helloDTO.setHelloI32(Integer.MAX_VALUE);
        helloDTO.setHelloI64(Long.MAX_VALUE);
        helloDTO.setHelloByte(Byte.MAX_VALUE);
        helloDTO.setHelloBool(Boolean.TRUE);
        helloDTO.setHelloDouble(Double.MAX_VALUE);
        helloDTO.setHelloMap(Collections.singletonMap(1, "1"));
        helloDTO.setHelloSet(Collections.singleton(1));
        helloDTO.setHelloList(Collections.singletonList(1));
        return helloDTO;
    }

    @Override
    public void helloVoid(String str) throws HelloException, TException {
        logger.info("helloVoid: {}", str);
    }
}
