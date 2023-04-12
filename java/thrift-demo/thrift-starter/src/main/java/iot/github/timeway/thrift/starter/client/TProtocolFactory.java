package iot.github.timeway.thrift.starter.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * TProtocolFactory
 *
 * @author Zexin Li
 * @date 2023-04-12 10:55
 */
@Getter
@AllArgsConstructor
public class TProtocolFactory implements PoolableObjectFactory<TProtocol> {
    /**
     * 服务端 域名/IP
     */
    private String serviceHost;
    /**
     * 服务端 端口号
     */
    private int servicePort;
    /**
     * Socket 超时时间（单位：ms）
     */
    private int socketTimeout;

    private Class<? extends TProtocol> protocolClazz;

    private Class<? extends TTransport> transportClazz;

    public TProtocolFactory(String serviceHost, int servicePort, int socketTimeout) {
        this(serviceHost, servicePort, socketTimeout, TBinaryProtocol.class, TFramedTransport.class);
    }

    /**
     * 创建对象
     *
     * @return
     * @throws Exception
     */
    @Override
    public TProtocol makeObject() throws Exception {
        try {
            TSocket tSocket = new TSocket(this.serviceHost, this.servicePort, this.socketTimeout);
            TTransport transport = transportClazz.getDeclaredConstructor(TTransport.class).newInstance(tSocket);
            transport.open();
            return protocolClazz.getDeclaredConstructor(TTransport.class).newInstance(transport);
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s:%s 连接失败", serviceHost, servicePort), e);
        }
    }

    /**
     * 销毁对象
     *
     * @param tProtocol the instance to be destroyed
     * @throws Exception
     */
    @Override
    public void destroyObject(TProtocol tProtocol) throws Exception {
        if (tProtocol.getTransport().isOpen()) {
            tProtocol.getTransport().close();
        }
    }

    /**
     * 检验对象是否可以由pool安全返回
     *
     * @param tProtocol
     * @return
     */
    @Override
    public boolean validateObject(TProtocol tProtocol) {
        return tProtocol.getTransport().isOpen();
    }

    /**
     * 激活对象
     *
     * @param tProtocol the instance to be activated
     * @throws Exception
     */
    @Override
    public void activateObject(TProtocol tProtocol) throws Exception {

    }

    /**
     * 使无效
     *
     * @param tProtocol the instance to be passivated
     * @throws Exception
     */
    @Override
    public void passivateObject(TProtocol tProtocol) throws Exception {

    }
}
