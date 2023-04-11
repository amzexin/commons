package iot.github.timeway.thrift.server;

import iot.github.timeway.thrift.server.service.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Main
 *
 * @author Zexin Li
 * @date 2023-04-11 17:33
 */
public class ServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);

    private static final int SERVER_PORT = 8090;

    public static void main(String[] args) {
        ServerStarter.start(SERVER_PORT, serviceImpls());
        log.info("========>>> successful!!! <<<========");
        log.info("========>>> successful!!! <<<========");
        log.info("========>>> successful!!! <<<========");
    }

    private static List<Object> serviceImpls() {
        return Arrays.asList(new HelloServiceImpl());
    }

}
