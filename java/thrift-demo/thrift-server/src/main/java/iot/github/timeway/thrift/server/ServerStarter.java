package iot.github.timeway.thrift.server;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main
 *
 * @author Zexin Li
 * @date 2023-04-11 17:33
 */
public class ServerStarter {

    private static final Logger log = LoggerFactory.getLogger(ServerStarter.class);

    private static TServer createServer(int serverPort, TMultiplexedProcessor processors) throws TTransportException {
        TNonblockingServerSocket nonblockingServerSocket = new TNonblockingServerSocket(serverPort);
        // 使用非阻塞式IO
        TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(nonblockingServerSocket);
        tnbArgs.processor(processors);
        tnbArgs.protocolFactory(new TBinaryProtocol.Factory());
        tnbArgs.transportFactory(new TFramedTransport.Factory());
        return new TNonblockingServer(tnbArgs);
    }

    private static TMultiplexedProcessor createProcessor(List<Object> serviceImpls) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        TMultiplexedProcessor processors = new TMultiplexedProcessor();
        Pattern pattern = Pattern.compile("^(.+)\\$Iface$");

        for (Object serviceImpl : serviceImpls) {
            // class iot.github.timeway.thrift.server.service.HelloServiceImpl
            Class<?> serviceImplClazz = serviceImpl.getClass();

            // 遍历所有实现类对应的接口
            for (Class<?> iFaceClazz : serviceImplClazz.getInterfaces()) {
                // interface iot.github.timeway.thrift.api.service.HelloService$Iface
                String iFaceClazzName = iFaceClazz.getName();

                // 过滤非 IFace 的接口
                Matcher iFaceClazzMatcher = pattern.matcher(iFaceClazzName);
                if (!iFaceClazzMatcher.find()) {
                    continue;
                }

                // iot.github.timeway.thrift.api.service.HelloService
                String serviceApiClazzName = iFaceClazzMatcher.group(1);

                // 创建对应的 TProcessor
                TProcessor processor = (TProcessor) Class.forName(serviceApiClazzName + "$Processor")
                        .getDeclaredConstructor(iFaceClazz)
                        .newInstance(serviceImpl);

                processors.registerProcessor(serviceApiClazzName, processor);
            }
        }
        return processors;
    }

    public static void start(int serverPort, List<Object> serviceImpls) {
        Thread thread = new Thread(() -> {
            try {
                TServer server = createServer(serverPort, createProcessor(serviceImpls));
                server.serve();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, "thrift-server");
        thread.setDaemon(false);
        thread.start();
    }

}
