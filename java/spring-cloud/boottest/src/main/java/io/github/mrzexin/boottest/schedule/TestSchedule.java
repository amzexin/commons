package io.github.mrzexin.boottest.schedule;

import io.github.mrzexin.boottest.annoation.TestAnnotation;
import io.github.mrzexin.boottest.config.ZookeeperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TestSchedule
 *
 * @author zexin
 */
@Component
public class TestSchedule {

    @Autowired
    private ZookeeperFactory zookeeperFactory;

    @TestAnnotation("${server.port}")
    private String port;

    @Scheduled(fixedDelay = 1000)
    public void test(){
        System.out.println(zookeeperFactory.toString());
        System.out.println(port);
    }
}
