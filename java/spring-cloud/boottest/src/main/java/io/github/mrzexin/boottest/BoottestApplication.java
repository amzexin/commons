package io.github.mrzexin.boottest;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class BoottestApplication {

    public static void main(String[] args) {
        MDC.put("trace_id", "main" + System.currentTimeMillis());
        SpringApplication.run(BoottestApplication.class, args);

        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        MDC.remove("trace_id");
    }

}