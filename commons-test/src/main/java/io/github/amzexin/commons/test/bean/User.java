package io.github.amzexin.commons.test.bean;

import lombok.Data;

import java.util.List;

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
    private List<Integer> list;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
