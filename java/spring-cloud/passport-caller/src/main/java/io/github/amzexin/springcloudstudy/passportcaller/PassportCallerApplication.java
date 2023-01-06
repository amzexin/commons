package io.github.amzexin.springcloudstudy.passportcaller;

import io.github.amzexin.springcloudstudy.passport.api.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableFeignClients(clients = {IUserService.class})
@EnableDiscoveryClient
@SpringBootApplication
public class PassportCallerApplication {

    public static void main(String[] args) {
        MDC.put("trace_id", "main" + System.currentTimeMillis());
        SpringApplication.run(PassportCallerApplication.class, args);

        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        MDC.remove("trace_id");
    }

}
