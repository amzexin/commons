package io.github.mrzexin.boottest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ZookeeperAutoconfiguration
 *
 * @author zexin
 */
@Configuration
@EnableConfigurationProperties({Zookeeper.class})
public class ZookeeperAutoconfiguration {

    @Autowired
    private Zookeeper zookeeper;

    @ConditionalOnMissingBean({ZookeeperFactory.class})
    @Bean
    public ZookeeperFactory zookeeperFactory(){
        ZookeeperFactory zookeeperFactory = new ZookeeperFactory();
        zookeeperFactory.setName("1");
        zookeeperFactory.setHosts(zookeeper.getHosts());
        return zookeeperFactory;
    }
}
