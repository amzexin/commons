package io.github.amzexin.springcloudstudy.passport.api.dto;

import lombok.Data;

@Data
public class GetUserResponseDTO {
    private int userId;
    private String username;
    private String password;
}
