package com.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.config.AuthAccess;
import com.springboot.entity.Files;
import com.springboot.mapper.FileMapper;
import com.springboot.service.IFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/*
*
* 文件上传相关接口
*
*  */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private IFileService fileService;
    @Resource
    private FileMapper fileMapper;
    /*
    *
    * 上传接口
    *
    * */
    @AuthAccess
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //先存储到磁盘
//        File uploadParentfile = new File(fileUploadPath);

        //定义一个文件唯一标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUuid);
        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        String url;
        //把获取到的文件存储到磁盘目录
        file.transferTo(uploadFile);
        //获取文件的md5
        String md5 = SecureUtil.md5(uploadFile);
        Files dbfiles = getFileMd5(md5);
        if (dbfiles != null){
            url = dbfiles.getUrl();
            uploadFile.delete();
        }else {
            url = "http://localhost:9090/file/" + fileUuid;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }
    /*
    *
    * 下载接口
    *
    * */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUuid);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUuid, StandardCharsets.UTF_8));
        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private Files getFileMd5(String md5){
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.isEmpty() ? null : filesList.get(0);
    }

    @GetMapping("/front/all")
    public Result frontAll() {
        return Result.success(fileService.list());
    }
    @PostMapping()
    public Result save(@RequestBody Files files) {
        return Result.success(fileService.save(files));
    }
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileService.updateById(files));
    }
    @GetMapping()
    public Result findAll() {
        return Result.success(fileService.list());
    }

//    @GetMapping("/{id}")
//    public Result findOne(@PathVariable Integer id) {
//        return Result.success(fileService.getById(id));
//    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {return Result.success(fileService.removeById(id));}

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {return Result.success(fileService.removeByIds(ids));}

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }
        return Result.success(fileService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
    }


