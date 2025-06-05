package com.kevin_cx.model.dto.application;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class ApplicationUpdateRequest implements Serializable {

	/**
	 * 申请ID，主键
	 */
	private Long id;

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


	private static final long serialVersionUID = 1L;
}