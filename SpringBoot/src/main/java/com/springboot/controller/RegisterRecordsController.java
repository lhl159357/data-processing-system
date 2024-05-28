package com.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.entity.Menu;
import com.springboot.entity.RegisterRecords;
import com.springboot.service.IRegisterRecordsService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/registerRecords")
public class RegisterRecordsController {

    @Autowired
    private IRegisterRecordsService registerRecordsService;
    @PostMapping()
    public Result save(@RequestBody RegisterRecords registerRecords) {
        return Result.success(registerRecordsService.saveOrUpdate(registerRecords));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(registerRecordsService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(registerRecordsService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {return Result.success(registerRecordsService.removeById(id));}

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {return Result.success(registerRecordsService.removeByIds(ids));}

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<RegisterRecords> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }
        return Result.success(registerRecordsService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<List<Object>> list = reader.read(1);
        List<RegisterRecords> registerRecord = CollUtil.newArrayList();
        Set<String> yzIdSet = new HashSet<>(); // 存储已经出现过的YzId

        for (List<Object> row : list) {
            String yzId = row.get(0).toString();
            // 如果集合中已经存在该YzId，则跳过这一行
            if (yzIdSet.contains(yzId)) {
                continue;
            }
            RegisterRecords registerRecords = new RegisterRecords();
            registerRecords.setYzId(yzId);
            registerRecords.setName(row.get(1).toString());
            registerRecords.setChannels(row.get(2).toString());
            registerRecords.setSource(row.get(3).toString());
            String registerTimeStr = row.get(4).toString();
            String loginTimeStr = row.get(5).toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date registerTime = dateFormat.parse(registerTimeStr);
            Date loginTime = dateFormat.parse(loginTimeStr);
            registerRecords.setRegisterTime(registerTime);
            registerRecords.setLoginTime(loginTime);
            registerRecord.add(registerRecords);
            // 将YzId添加到集合中
            yzIdSet.add(yzId);
        }
        registerRecordsService.saveBatch(registerRecord);
        return true;
    }


    @GetMapping("/export")
    public void export (HttpServletResponse response) throws Exception {
        List<RegisterRecords> list = registerRecordsService.list();
        //通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(fileUploadPath + "/用户信息.xlsx");

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("yzId","远智编码");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("channels","注册渠道");
        writer.addHeaderAlias("source","注册来源");
        writer.addHeaderAlias("registerTime","注册时间");
        writer.addHeaderAlias("loginTime","登录时间");

        //一次性写出list内的对象excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedecument.spreadsheetml,sheet;charset=utf-8");
        String fileName = URLEncoder.encode("新注册用户登录记录","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName +".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream,true);
        outputStream.close();
        writer.close();
    }
}
