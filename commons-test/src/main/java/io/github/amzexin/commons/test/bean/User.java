package io.github.amzexin.commons.test.bean;

import lombok.Data;

/**
 * User
 *
 * @author zexin
 */
@Data
public class User {
    private String name;
    private Integer age;
    private Address address;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
