package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2024-03-31
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
