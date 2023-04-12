package iot.github.timeway.thrift.server;

import iot.github.timeway.thrift.api.service.HelloService;
import iot.github.timeway.thrift.server.service.HelloServiceImpl;
import iot.github.timeway.thrift.starter.server.ServerStarter;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main
 *
 * @author Zexin Li
 * @date 2023-04-11 17:33
 */
public class ServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);

    private static final int SERVER_PORT = 8090;

    private static boolean USE_MULTIPLEXED_PROCESSOR = false;

    private static void simpleTest(int serverPort) throws TException {
        TNonblockingServerSocket nonblockingServerSocket = new TNonblockingServerSocket(serverPort);
        TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(nonblockingServerSocket);
        tnbArgs.protocolFactory(new TBinaryProtocol.Factory());
        tnbArgs.transportFactory(new TFramedTransport.Factory());
        if (USE_MULTIPLEXED_PROCESSOR) {
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
            multiplexedProcessor.registerProcessor(HelloService.class.getName(), new HelloService.Processor<>(new HelloServiceImpl()));
            tnbArgs.processor(multiplexedProcessor);
        } else {
            tnbArgs.processor(new HelloService.Processor<>(new HelloServiceImpl()));
        }

        TServer server = new TNonblockingServer(tnbArgs);
        new Thread(server::serve).start();
    }

    private static void frameworkTest(int serverPort) throws TException {
        ServerStarter serverStarter = new ServerStarter(serverPort);
        serverStarter.start(new HelloServiceImpl(), USE_MULTIPLEXED_PROCESSOR);
    }

    public static void main(String[] args) throws TException {
        simpleTest(SERVER_PORT);
        frameworkTest(SERVER_PORT + 1);
        log.info("========>>> successful!!! <<<========");
        log.info("========>>> successful!!! <<<========");
        log.info("========>>> successful!!! <<<========");
    }

}
