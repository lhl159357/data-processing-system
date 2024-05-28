package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhl
 * @since 2024-05-20
 */
@Data
@TableName("monthly_active_user")
public class MonthlyActiveUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sendUser")
    private String sendUser;

    @TableField("activityUser")
    private String activityUser;

    @TableField("customUser")
    private String customUser;

    @TableField("shareUser")
    private String shareUser;

    @TableField("likeUser")
    private String likeUser;

    @TableField("commentUser")
    private String commentUser;

    @TableField("payUser")
    private String payUser;

    @TableField("signInUser")
    private String signInUser;

    @TableField("rewardUser")
    private String rewardUser;

    @TableField("shareActUser")
    private String shareActUser;

    @TableField("removingUser")
    private String removingUser;

    @TableField("createTime")
    private LocalDateTime createTime;


}
