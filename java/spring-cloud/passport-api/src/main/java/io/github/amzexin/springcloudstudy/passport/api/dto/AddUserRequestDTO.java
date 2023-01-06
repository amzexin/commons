package io.github.amzexin.springcloudstudy.passport.api.dto;

import lombok.Data;

@Data
public class AddUserRequestDTO {
    private String username;
    private String password;
}
