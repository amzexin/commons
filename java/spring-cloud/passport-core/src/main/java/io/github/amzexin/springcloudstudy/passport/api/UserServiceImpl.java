package io.github.amzexin.springcloudstudy.passport.api;

import io.github.amzexin.springcloudstudy.passport.api.dto.*;
import io.github.amzexin.springcloudstudy.passport.api.service.IUserService;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class UserServiceImpl implements IUserService {

    @Override
    public CommonResult<AddUserResponseDTO> addUser(AddUserRequestDTO addUserRequestDTO) {
        AddUserResponseDTO responseDTO = new AddUserResponseDTO();
        responseDTO.setUserId(Integer.parseInt(new SimpleDateFormat("ss").format(new Date())));
        return CommonResult.success(responseDTO);
    }

    @Override
    public CommonResult<Object> deleteUser(DeleteUserRequestDTO deleteUserRequestDTO) {
        return CommonResult.success();
    }

    @Override
    public CommonResult<Object> updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        return CommonResult.success();
    }

    @Override
    public CommonResult<QueryUserResponseDTO> queryUser(QueryUserRequestDTO queryUserRequestDTO) {
        QueryUserResponseDTO responseDTO = new QueryUserResponseDTO();
        responseDTO.setUserId(Integer.parseInt(new SimpleDateFormat("ss").format(new Date())));
        responseDTO.setUsername(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        responseDTO.setPassword(System.currentTimeMillis() + "");
        return CommonResult.success(responseDTO);
    }

    @Override
    public CommonResult<GetUserResponseDTO> getUser(int userId) {
        GetUserResponseDTO responseDTO = new GetUserResponseDTO();
        responseDTO.setUserId(Integer.parseInt(new SimpleDateFormat("ss").format(new Date())));
        responseDTO.setUsername(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        responseDTO.setPassword(System.currentTimeMillis() + "");
        return CommonResult.success(responseDTO);
    }

}
