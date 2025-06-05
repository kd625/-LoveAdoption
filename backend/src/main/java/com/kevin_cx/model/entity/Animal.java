package com.kevin_cx.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName animal
 */
@TableName(value ="animal")
@Data
public class Animal implements Serializable {
    /**
     * 动物ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 动物的名字
     */
    private String name;

    /**
     * 动物物种
     */
    private String species;

    /**
     * 动物年龄
     */
    private Integer age;

    /**
     * 动物性别(0公，1母）
     */
    private Integer gender;

    /**
     * 动物的描述
     */
    private String description;

    /**
     * 动物健康状况
     */
    private String health_status;

    /**
     * 动物领养状态，0 表示可领养，1 表示已领养 ,2代表已删除
     */
    private Integer adoption_status;

    /**
     * 动物图片的URL
     */
    private String image_url;

    /**
     * 动物记录创建时间
     */
    private Date created_time;

    /**
     * 记录更新时间
     */
    private Date updated_time;

    /**
     * 软删除时间，标记动物记录删除
     */
    private Date deleted_time;

    /**
     * 大的动物品种（狗1，猫2，鸟3，兔4...)
     */
    private String breed;

    /**
     * 发布者用户id
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}