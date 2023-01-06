package io.github.mrzexin.boottest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Zookeeper
 *
 * @author zexin
 */
@ConfigurationProperties(prefix = "zookeeper")
@Data
public class Zookeeper {
    private String hosts;
    private String connectionTimeout;
    private String sessionTimeout;
    private String singleton;
}
