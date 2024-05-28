package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhl
 * @since 2024-03-31
 */
@Data
@TableName("menu")
public class Menu {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("path")
    private String path;

    @TableField("icon")
    private String icon;

    @TableField("description")
    private String description;

    @TableField("pid")
    private Integer pid;

    @TableField("page_path")
    private String pagePath;

    @TableField(exist = false)
    private List<Menu> children;

}
