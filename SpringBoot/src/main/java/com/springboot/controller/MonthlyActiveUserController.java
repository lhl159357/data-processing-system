package com.springboot.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.entity.ActiveUsersData;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.springboot.service.IMonthlyActiveUserService;
import com.springboot.entity.MonthlyActiveUser;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhl
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/monthlyActiveUser")
    public class MonthlyActiveUserController {
        @Autowired
        private IMonthlyActiveUserService monthlyActiveUserService;
        @GetMapping
        public Result findAll() {
        return Result.success(monthlyActiveUserService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
        return Result.success(monthlyActiveUserService.getById(id));
        }

        @PostMapping
        public Result save(@RequestBody MonthlyActiveUser monthlyActiveUser) {
        return Result.success(monthlyActiveUserService.saveOrUpdate(monthlyActiveUser));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {return Result.success(monthlyActiveUserService.removeById(id));
        }
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {

        return Result.success(monthlyActiveUserService.removeByIds(ids));

        }
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize) {
        QueryWrapper<MonthlyActiveUser> queryWrapper = new QueryWrapper<>();
        return Result.success(monthlyActiveUserService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }

        @PostMapping("/import")
        public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<RegisterRecords> list = reader.readAll(RegisterRecords.class);
        List<List<Object>> list = reader.read(1);
        List<MonthlyActiveUser> monthlyActiveUser = CollUtil.newArrayList();
        System.out.println(list);
        for (List<Object> row : list) {
            MonthlyActiveUser monthlyActiveUser1 = new MonthlyActiveUser();
            monthlyActiveUser1.setSendUser(row.get(0).toString());
            monthlyActiveUser1.setActivityUser(row.get(1).toString());
            monthlyActiveUser1.setCustomUser(row.get(2).toString());
            monthlyActiveUser1.setShareUser(row.get(3).toString());
            monthlyActiveUser1.setLikeUser(row.get(4).toString());
            monthlyActiveUser1.setCommentUser(row.get(5).toString());
            monthlyActiveUser1.setPayUser(row.get(6).toString());
            monthlyActiveUser1.setSignInUser(row.get(7).toString());
            monthlyActiveUser1.setRewardUser(row.get(8).toString());
            monthlyActiveUser1.setShareActUser(row.get(9).toString());
            monthlyActiveUser1.setRemovingUser(row.get(10).toString());
            monthlyActiveUser.add(monthlyActiveUser1);
        }
        monthlyActiveUserService.saveBatch(monthlyActiveUser);
        return true;
    }

        @GetMapping("/export")
        public void export (HttpServletResponse response) throws Exception {
        List<MonthlyActiveUser> list = monthlyActiveUserService.list();
        //通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(fileUploadPath + "/用户信息.xlsx");

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("sendUser","发帖用户");
        writer.addHeaderAlias("activityUser","上进活动人数");
        writer.addHeaderAlias("customUser","打卡上进习惯人数");
        writer.addHeaderAlias("shareUser","帖子分享人数");
        writer.addHeaderAlias("likeUser","帖子点赞人数");
        writer.addHeaderAlias("commentUser","帖子评论人数");
        writer.addHeaderAlias("payUser","商城下单人数");
        writer.addHeaderAlias("signInUser","每日签到人数");
        writer.addHeaderAlias("rewardUser","直播广场打赏");
        writer.addHeaderAlias("shareActUser","分享活动人数");
        writer.addHeaderAlias("removingUser","行为合计去重人数");

        //一次性写出list内的对象excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedecument.spreadsheetml,sheet;charset=utf-8");
        String fileName = URLEncoder.encode("活跃用户参与率","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName +".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);
        outputStream.close();
        writer.close();
    }
    }

