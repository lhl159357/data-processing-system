package com.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.entity.Role;
import com.springboot.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhl
 * @since 2024-03-31
 */
@RestController
@RequestMapping("/role")
    public class RoleController {
        @Autowired
        private IRoleService roleService;
        @GetMapping
        public Result findAll() {
        return Result.success(roleService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
        }

        @PostMapping
        public Result save(@RequestBody Role role) {
        return Result.success(roleService.saveOrUpdate(role));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {return Result.success(roleService.removeById(id));
        }
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {

        return Result.success(roleService.removeByIds(ids));

        }
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)){
        queryWrapper.like("name", name);
        }

        return Result.success(roleService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }

        @PostMapping("/roleMenu/{roleId}")
        public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds) {
            roleService.setRoleMenu(roleId, menuIds);
            return Result.success();
        }
        @GetMapping("/roleMenu/{roleId}")
        public Result getRoleMenu(@PathVariable Integer roleId) {

            return Result.success(roleService.getRoleMenu(roleId));
    }
        }

