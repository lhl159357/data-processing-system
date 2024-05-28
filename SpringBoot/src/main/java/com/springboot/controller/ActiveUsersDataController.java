package com.springboot.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.entity.RegisterRecords;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.springboot.service.IActiveUsersDataService;
import com.springboot.entity.ActiveUsersData;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhl
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/activeUsersData")
    public class ActiveUsersDataController {
        @Autowired
        private IActiveUsersDataService activeUsersDataService;
        @GetMapping
        public Result findAll() {
        return Result.success(activeUsersDataService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
        return Result.success(activeUsersDataService.getById(id));
        }

        @PostMapping
        public Result save(@RequestBody ActiveUsersData activeUsersData) {
        return Result.success(activeUsersDataService.saveOrUpdate(activeUsersData));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {return Result.success(activeUsersDataService.removeById(id));
        }
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {

        return Result.success(activeUsersDataService.removeByIds(ids));

        }
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam(defaultValue = "") String date) {
        QueryWrapper<ActiveUsersData> queryWrapper = new QueryWrapper<>();
        if (!"".equals(date)){
        queryWrapper.like("date",date);
        }

        return Result.success(activeUsersDataService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }

    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<RegisterRecords> list = reader.readAll(RegisterRecords.class);
        List<List<Object>> list = reader.read(1);
        List<ActiveUsersData> activeUserData = CollUtil.newArrayList();
        System.out.println(list);
        for (List<Object> row : list) {
            ActiveUsersData activeUsersData = new ActiveUsersData();
            activeUsersData.setDate(row.get(0).toString());
            activeUsersData.setActiveUser(row.get(1).toString());
            activeUsersData.setSendUser(row.get(2).toString());
            activeUsersData.setAttendUser(row.get(3).toString());
            activeUsersData.setCardUser(row.get(4).toString());
            activeUsersData.setLikeUser(row.get(5).toString());
            activeUsersData.setCommentUser(row.get(6).toString());
            activeUsersData.setShareUser(row.get(7).toString());
            activeUsersData.setRewardUser(row.get(8).toString());
            activeUsersData.setPayUser(row.get(9).toString());
            activeUsersData.setOtherUser(row.get(10).toString());
            activeUsersData.setTotal(row.get(11).toString());
            activeUserData.add(activeUsersData);
        }
        activeUsersDataService.saveBatch(activeUserData);
        return true;
    }

    @GetMapping("/export")
    public void export (HttpServletResponse response) throws Exception {
        List<ActiveUsersData> list = activeUsersDataService.list();
        //通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(fileUploadPath + "/用户信息.xlsx");

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("date","日期");
        writer.addHeaderAlias("activeUser","日活跃用户");
        writer.addHeaderAlias("sendUser","日发帖用户数");
        writer.addHeaderAlias("attendUser","日上进活动报名人数");
        writer.addHeaderAlias("cardUser","日上进习惯打卡人数");
        writer.addHeaderAlias("likeUser","日圈子帖子点赞人数");
        writer.addHeaderAlias("commentUser","日圈子帖子评论人数");
        writer.addHeaderAlias("shareUser","日圈子帖子分享人数");
        writer.addHeaderAlias("payUser","日商城下单人数");
        writer.addHeaderAlias("rewardUser","日打赏人数");
        writer.addHeaderAlias("otherUser","日圈子总分享人数_含上进故事等");
        writer.addHeaderAlias("total","合计");

        //一次性写出list内的对象excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedecument.spreadsheetml,sheet;charset=utf-8");
        String fileName = URLEncoder.encode("APP活跃用户的社区日活行为数据","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName +".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);
        outputStream.close();
        writer.close();
    }
    }

