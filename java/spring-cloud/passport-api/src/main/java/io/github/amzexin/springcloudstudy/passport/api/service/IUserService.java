package io.github.amzexin.springcloudstudy.passport.api.service;

import io.github.amzexin.springcloudstudy.passport.api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("passport-service")
public interface IUserService {

    @PostMapping("/user/add")
    CommonResult<AddUserResponseDTO> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO);

    @PostMapping("/user/delete")
    CommonResult<Object> deleteUser(@RequestBody DeleteUserRequestDTO deleteUserRequestDTO);

    @PostMapping("/user/update")
    CommonResult<Object> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO);

    @GetMapping("/user/query")
    CommonResult<QueryUserResponseDTO> queryUser(@RequestBody QueryUserRequestDTO queryUserRequestDTO);

    @GetMapping("/user/get/{userId}")
    CommonResult<GetUserResponseDTO> getUser(@PathVariable("userId") int userId);

}
