package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.controller.dto.UserDTO;
import com.springboot.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2024-03-08
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
