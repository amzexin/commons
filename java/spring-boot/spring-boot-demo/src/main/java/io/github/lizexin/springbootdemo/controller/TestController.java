package io.github.lizexin.springbootdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author Zexin Li
 * @date 2023-01-09 15:11
 */
@RestController
public class TestController {

    @GetMapping("/ping")
    public Object ping() {
        return "pong: " + System.currentTimeMillis();
    }
}
