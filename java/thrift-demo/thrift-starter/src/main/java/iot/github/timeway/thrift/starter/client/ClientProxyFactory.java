package iot.github.timeway.thrift.starter.client;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.protocol.TProtocol;

/**
 * ClientStarter
 *
 * @author Zexin Li
 * @date 2023-04-12 10:05
 */
public class ClientProxyFactory {

    private final ObjectPool<TProtocol> protocolPool;

    public ClientProxyFactory(TProtocolFactory protocolFactory) {
        this.protocolPool = new GenericObjectPool<>(protocolFactory);
    }

    public <T> T makeProxy(Class<T> clazz, boolean useMultiplexedProcessor) {
        return ClientProxy.newInstance(clazz, protocolPool, useMultiplexedProcessor);
    }
}
