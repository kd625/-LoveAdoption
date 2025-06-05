package com.kevin_cx.model.dto.animal;

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
public class AnimalQueryRequest extends PageRequest implements Serializable {


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
	 * 动物健康状况
	 */
	private String health_status;

	/**
	 * 动物领养状态，0 表示可领养，1 表示已领养 ,2代表已删除
	 */
	private Integer adoption_status;

	/**
	 * 动物记录创建时间
	 */
	private Date created_time;

	/**
	 * 记录更新时间
	 */
	private Date updated_time;

	/**
	 * 大的动物品种（狗1，猫2，鸟3，兔4...)
	 */
	private String breed;

	/**
	 * 发布者用户id
	 */
	private Long userId;


	private static final long serialVersionUID = 1L;
}