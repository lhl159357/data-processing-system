    package com.springboot.controller;

    import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import com.springboot.common.Constants;
    import com.springboot.common.Result;
    import com.springboot.controller.dto.UserDTO;
    import com.springboot.entity.User;
    import com.springboot.service.IUserService;
    import com.springboot.utils.TokenUtils;
    import org.apache.commons.lang3.StringUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;


    /**
     * <p>
     *  前端控制器
     * </p>
     *
     * @author lhl
     * @since 2024-03-08
     */
    @RestController
    @RequestMapping("/user")
        public class UserController {
            @Autowired
            private IUserService userService;

            @PostMapping("/login")
            public Result login(@RequestBody UserDTO userDTO){
                String username = userDTO.getUsername();
                String password = userDTO.getPassword();
                if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                    return Result.error(Constants.CODE_400,"参数错误");
                }
                UserDTO dto = userService.login(userDTO);
                return Result.success(dto);
            }
            @PostMapping("/register")
            public Result register(@RequestBody UserDTO userDTO) {
                String username = userDTO.getUsername();
                String password = userDTO.getPassword();
                if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                    return Result.error(Constants.CODE_400,"参数错误");

                }
                return Result.success(userService.register(userDTO));

        }
            @GetMapping("/username/{username}")
            public Result findOne(@PathVariable String username) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username",username);
            return Result.success(userService.getOne(queryWrapper));
        }
            @PostMapping()
            public Result save(@RequestBody User user) {
            return Result.success(userService.saveOrUpdate(user));
            }

            @GetMapping
            public Result findAll() {
            return Result.success(userService.list());
        }

            @GetMapping("/{id}")
            public Result findOne(@PathVariable Integer id) {
            return Result.success(userService.getById(id));
        }

            @DeleteMapping("/{id}")
            public Result delete(@PathVariable Integer id) {return Result.success(userService.removeById(id));}

            @PostMapping("/del/batch")
            public Result deleteBatch(@RequestBody List<Integer> ids) {return Result.success(userService.removeByIds(ids));}

            @GetMapping("/page")
            public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            if (!"".equals(username)){
            queryWrapper.like("username",username);
            }
            //获取当前用户信息
                User currentUser = TokenUtils.getCurrentUser();
            System.out.println("获取当前用户信息===============" + currentUser.getUsername());
            return Result.success(userService.page(new Page<>(pageNum, pageSize),queryWrapper));
            }
        }

