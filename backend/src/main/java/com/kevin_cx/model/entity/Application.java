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
 * @TableName application
 */
@TableName(value ="application")
@Data
public class Application implements Serializable {
    /**
     * 申请ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 申请用户ID，关联user表
     */
    private Long userId;

    /**
     * 申请领养的动物ID，关联animal表
     */
    private Long animalId;

    /**
     * 用户申请理由（如喜爱动物、有经验等）
     */
    private String application_reason;

    /**
     * 申请状态，0：待审核，1：通过，2：拒绝，3：已撤回
     */
    private Integer status;

    /**
     * 审核备注（管理员审批意见）
     */
    private String review_comments;

    /**
     * 审核管理员ID，关联user表
     */
    private Long reviewed_by;

    /**
     * 申请提交时间
     */
    private Date applicationTime;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 记录创建时间
     */
    private Date createdTime;

    /**
     * 记录更新时间
     */
    private Date updatedTime;

    /**
     * 软删除时间
     */
    private Date deletedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}