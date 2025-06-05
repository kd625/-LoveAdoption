package com.kevin_cx.model.dto.application;

import com.kevin_cx.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author kevinCx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationQueryRequest extends PageRequest implements Serializable {


	/**
	 * 申请ID，主键
	 */
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
	private Integer reviewed_by;

	/**
	 * 申请提交时间
	 */
	private Date application_time;

	/**
	 * 审核时间
	 */
	private Date review_time;

	/**
	 * 记录创建时间
	 */
	private Date created_time;

	/**
	 * 记录更新时间
	 */
	private Date updated_time;


	private static final long serialVersionUID = 1L;
}