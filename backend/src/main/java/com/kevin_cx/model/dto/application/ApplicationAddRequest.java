package com.kevin_cx.model.dto.application;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class ApplicationAddRequest implements Serializable {


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


	private static final long serialVersionUID = 1L;
}