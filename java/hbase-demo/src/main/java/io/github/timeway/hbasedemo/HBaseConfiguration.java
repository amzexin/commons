package io.github.timeway.hbasedemo;

import lombok.Data;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;

/**
 * HBaseConfiguration
 *
 * @author Zexin Li
 * @date 2023-04-10 11:36
 */
@Data
public class HBaseConfiguration {
    /**
     * @see org.apache.hadoop.hbase.HConstants#ZOOKEEPER_QUORUM
     */
    private String zookeeper_quorum;
    /**
     * @see org.apache.hadoop.hbase.HConstants#ZOOKEEPER_CLIENT_PORT
     */
    private String zookeeper_client_port;
    /**
     * @see org.apache.hadoop.hbase.HConstants#ZOOKEEPER_ZNODE_PARENT
     */
    private String zookeeper_znode_parent;

    public Configuration toOrigin() {
        Configuration conf = new Configuration();
        if (zookeeper_quorum != null) {
            conf.set(HConstants.ZOOKEEPER_QUORUM, zookeeper_quorum);
        }
        if (zookeeper_client_port != null) {
            conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, zookeeper_client_port);
        }
        if (zookeeper_znode_parent != null) {
            conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, zookeeper_znode_parent);
        }
        return conf;
    }
}
