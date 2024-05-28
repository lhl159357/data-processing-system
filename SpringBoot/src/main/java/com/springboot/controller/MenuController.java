package com.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.common.Constants;
import com.springboot.common.Result;
import com.springboot.entity.Dict;
import com.springboot.entity.Menu;
import com.springboot.mapper.DictMapper;
import com.springboot.service.IMenuService;
import jakarta.annotation.Resource;
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
@RequestMapping("/menu")
    public class MenuController {
        @Resource
        private IMenuService menuService;

        @Resource
        private DictMapper dictMapper;

        @GetMapping
        public Result findAll(@RequestParam(defaultValue = "") String name) {


            return Result.success(menuService.findMenus(name));
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
        }

        @PostMapping
        public Result save(@RequestBody Menu menu) {
        return Result.success(menuService.saveOrUpdate(menu));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {return Result.success(menuService.removeById(id));
        }
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(menuService.removeByIds(ids));

        }
        @GetMapping("/ids")
        public Result findAllIds(){
            return Result.success(menuService.list().stream().map(Menu::getId));
        }


        @GetMapping("/icons")
        public Result getIcons() {
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
            return Result.success(dictMapper.selectList(queryWrapper));
    }
}


