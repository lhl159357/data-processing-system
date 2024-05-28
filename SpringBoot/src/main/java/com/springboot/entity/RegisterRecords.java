package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import java.util.Date;

@Data
@ToString
public class RegisterRecords {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("yz_id")
    private String yzId;

    @TableField("name")
    private String name;

    private String channels;

    private String source;

    @TableField("registerTime")
    private Date registerTime;

    @TableField("loginTime")
    private Date loginTime;

}
