package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2024-05-15
 */
@Data
@TableName("active_users_data")
public class ActiveUsersData {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String date;

    @TableField("activeUser")
    private String activeUser;

    @TableField("sendUser")
    private String sendUser;

    @TableField("attendUser")
    private String attendUser;

    @TableField("cardUser")
    private String cardUser;

    @TableField("likeUser")
    private String likeUser;

    @TableField("commentUser")
    private String commentUser;

    @TableField("shareUser")
    private String shareUser;

    @TableField("payUser")
    private String payUser;

    @TableField("rewardUser")
    private String rewardUser;

    @TableField("otherUser")
    private String otherUser;

    @TableField("total")
    private String total;


}
