package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2024-03-31
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
