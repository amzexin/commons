package iot.github.timeway.thrift.starter.client;

import org.apache.commons.pool.ObjectPool;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ThriftClientProxy
 *
 * @author Zexin Li
 * @date 2023-04-12 13:35
 */
public class ClientProxy implements InvocationHandler {

    private final ObjectPool<TProtocol> protocolPool;

    private final Class<?> serviceClientClass;

    private final String serviceClassName;

    private final boolean useMultiplexedProcessor;

    public ClientProxy(Class<?> serviceClientClass, ObjectPool<TProtocol> protocolPool, boolean useMultiplexedProcessor) {
        this.serviceClientClass = serviceClientClass;
        this.serviceClassName = serviceClientClass.getName().replace("$Client", "");
        this.protocolPool = protocolPool;
        this.useMultiplexedProcessor = useMultiplexedProcessor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TProtocol baseProtocol = protocolPool.borrowObject();
        try {
            if (!useMultiplexedProcessor) {
                return method.invoke(serviceClientClass.getConstructor(TProtocol.class).newInstance(baseProtocol), args);
            }
            TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(baseProtocol, serviceClassName);
            return method.invoke(serviceClientClass.getConstructor(TProtocol.class).newInstance(multiplexedProtocol), args);
        } catch (Exception e) {
            protocolPool.invalidateObject(baseProtocol);
            throw new RuntimeException(e);
        } finally {
            protocolPool.returnObject(baseProtocol);
        }
    }

    private static Class<?> faceClassToClientClass(Class<?> clazz) {
        String faceClassName = clazz.getName();
        if (!clazz.isInterface()) {
            throw new RuntimeException(String.format("%s is not Interface", faceClassName));
        }

        if (!faceClassName.endsWith("$Iface")) {
            throw new RuntimeException(String.format("%s is not Thrift Iface", faceClassName));
        }

        String clientClassName = faceClassName.replace("$Iface", "$Client");
        try {
            return Class.forName(clientClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, ObjectPool<TProtocol> protocolPool, boolean useMultiplexedProcessor) {
        Class<?> serviceClientClass = faceClassToClientClass(clazz);
        return (T) Proxy.newProxyInstance(ClientProxy.class.getClassLoader(),
                serviceClientClass.getInterfaces(),
                new ClientProxy(serviceClientClass, protocolPool, useMultiplexedProcessor));
    }
}
