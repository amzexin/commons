package iot.github.timeway.thrift.starter.server;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
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

    private static final Pattern IFACE_PATTERN = Pattern.compile("^(.+)\\$Iface$");

    private int serverPort;

    private Class<? extends TProtocol> protocolClass;

    private Class<? extends TTransport> transportClass;

    public ServerStarter(int serverPort, Class<? extends TProtocol> protocolClass, Class<? extends TTransport> transportClass) {
        this.serverPort = serverPort;
        this.protocolClass = protocolClass;
        this.transportClass = transportClass;
    }

    public ServerStarter(int serverPort) {
        this(serverPort, TBinaryProtocol.class, TFramedTransport.class);
    }

    private TServer createServer(TProcessor processor) throws TTransportException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TNonblockingServerSocket nonblockingServerSocket = new TNonblockingServerSocket(serverPort);
        // 使用非阻塞式IO
        TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(nonblockingServerSocket);
        tnbArgs.processor(processor);
        tnbArgs.protocolFactory((TProtocolFactory) Class.forName(protocolClass.getName() + "$Factory").getConstructor().newInstance());
        tnbArgs.transportFactory((TTransportFactory) Class.forName(transportClass.getName() + "$Factory").getConstructor().newInstance());
        return new TNonblockingServer(tnbArgs);
    }

    /**
     * 服务端创建 TMultiplexedProcessor
     * 客户端也必须使用 TMultiplexedProtocol
     *
     * @param serviceImpls
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private TProcessor createMultiplexedProcessor(List<Object> serviceImpls) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
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
                String serviceClassName = iFaceClazzMatcher.group(1);

                // 创建对应的 TProcessor
                TProcessor processor = (TProcessor) Class.forName(serviceClassName + "$Processor")
                        .getDeclaredConstructor(iFaceClazz)
                        .newInstance(serviceImpl);

                // 客户端要和服务端保持一致
                multiplexedProcessor.registerProcessor(serviceClassName, processor);
            }
        }
        return multiplexedProcessor;
    }

    /**
     * 服务端创建 TProcessor
     * 客户端也必须使用 TProtocol
     *
     * @param serviceImpl
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private TProcessor createProcessor(Object serviceImpl) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // class iot.github.timeway.thrift.server.service.HelloServiceImpl
        Class<?> serviceImplClazz = serviceImpl.getClass();

        // 遍历所有实现类对应的接口
        for (Class<?> iFaceClazz : serviceImplClazz.getInterfaces()) {
            // interface iot.github.timeway.thrift.api.service.HelloService$Iface
            String iFaceClazzName = iFaceClazz.getName();

            // 过滤非 IFace 的接口
            Matcher iFaceClazzMatcher = IFACE_PATTERN.matcher(iFaceClazzName);
            if (!iFaceClazzMatcher.find()) {
                continue;
            }

            // iot.github.timeway.thrift.api.service.HelloService
            String serviceClassName = iFaceClazzMatcher.group(1);

            // 创建对应的 TProcessor
            return (TProcessor) Class.forName(serviceClassName + "$Processor")
                    .getDeclaredConstructor(iFaceClazz)
                    .newInstance(serviceImpl);

        }
        return null;
    }

    /**
     * 启动Server
     *
     * @param serviceImpls
     */
    public void start(List<Object> serviceImpls) {
        Thread thread = new Thread(() -> {
            try {
                TServer server = createServer(createMultiplexedProcessor(serviceImpls));
                server.serve();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, "multiplexed-thrift-server");
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * 启动Server
     *
     * @param serviceImpl
     * @param useMultiplexedProcessor 是否使用 TMultiplexedProcessor
     */
    public void start(Object serviceImpl, boolean useMultiplexedProcessor) {
        Thread thread = new Thread(() -> {
            try {
                TServer server = createServer(useMultiplexedProcessor ?
                        createMultiplexedProcessor(Collections.singletonList(serviceImpl)) :
                        createProcessor(serviceImpl));

                server.serve();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, "single-thrift-server");
        thread.setDaemon(false);
        thread.start();
    }

}
