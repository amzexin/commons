package iot.github.timeway.thrift.client;

import iot.github.timeway.thrift.api.bean.HelloDTO;
import iot.github.timeway.thrift.api.service.HelloService;
import iot.github.timeway.thrift.starter.client.ClientProxyFactory;
import iot.github.timeway.thrift.starter.client.TProtocolFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main
 *
 * @author Zexin Li
 * @date 2023-04-11 17:33
 */
public class ClientApplication {

    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);

    private static final String SERVER_HOST = "127.0.0.1";

    private static final int SERVER_PORT = 8090;

    private static final int SOCKET_TIMEOUT = 10 * 1000;

    private static boolean USE_MULTIPLEXED_PROCESSOR = false;

    private static void simpleTest(int serverPort) throws TException {
        try (TSocket tsocket = new TSocket(SERVER_HOST, serverPort)) {
            TTransport transport = new TFramedTransport(tsocket);
            TProtocol protocol = new TBinaryProtocol(transport);
            transport.open();

            HelloService.Client client = new HelloService.Client(USE_MULTIPLEXED_PROCESSOR ?
                    new TMultiplexedProtocol(protocol, HelloService.class.getName()) :
                    protocol);

            HelloDTO helloDTO = client.hello("hello");
            System.out.println(helloDTO);
        }
    }

    private static void frameworkTest(int serverPort) throws TException {
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory(new TProtocolFactory(SERVER_HOST, serverPort, SOCKET_TIMEOUT));

        HelloService.Iface helloService = clientProxyFactory.makeProxy(HelloService.Iface.class, USE_MULTIPLEXED_PROCESSOR);

        HelloDTO helloDTO = helloService.hello("hello");
        System.out.println(helloDTO);
    }

    public static void main(String[] args) throws TException, IOException {
        simpleTest(SERVER_PORT);
        System.in.read();
        frameworkTest(SERVER_PORT + 1);
    }

}
