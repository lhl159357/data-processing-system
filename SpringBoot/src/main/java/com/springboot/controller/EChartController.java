package com.springboot.controller;

import com.springboot.common.Result;
import com.springboot.mapper.ActiveUsersDataMapper;
import com.springboot.mapper.RegisterRecordsMapper;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/eChart")
public class EChartController {
    @Resource
    public RegisterRecordsMapper registerRecordsMapper;

    @Resource
    public  ActiveUsersDataMapper activeUsersDataMapper;

// 假设 Result 和 registerRecordsMapper 已经定义

    @GetMapping("/registerRecords/{startDate}/{endDate}")
    public Result getRRData(@PathVariable String startDate, @PathVariable String endDate) {
        List<Map<String, Object>> result = registerRecordsMapper.getOptionData(startDate, endDate);

        // 格式化 registerTime 属性为日期部分的字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.forEach(record -> {
            java.sql.Date date = (java.sql.Date) record.get("registerTime");
            String formattedDate = dateFormat.format(date);
            record.put("registerTime", formattedDate);
        });

        // 按 registerTime 进行分组
        Map<String, List<Map<String, Object>>> groupedResult = result.stream()
                .collect(Collectors.groupingBy(record -> (String) record.get("registerTime")));

        // 删除组内的 registerTime 属性
        groupedResult.values().forEach(group -> group.forEach(record -> record.remove("registerTime")));

        return Result.success(groupedResult);
    }

    @GetMapping("/activeUsersData/{startDate}/{endDate}")
    public Result getAUData(@PathVariable String startDate, @PathVariable String endDate) {
        List<Map<String, Object>> result = activeUsersDataMapper.getAUData(startDate, endDate);

        // 移除每个Map中的id和total字段
        for (Map<String, Object> item : result) {
            item.remove("id");
            item.remove("total");
        }

        return Result.success(result);
    }

}
