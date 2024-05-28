package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Dict {
    private String name;
    private String value;
    private String type;

}
