package com.kevin_cx.exception;

import com.kevin_cx.common.ErrorCode;

public class ThrowUtils {

	/**
	 * 条件成立则抛出异常
	 * @param condition 条件
	 * @param exception 异常
	 */
	public static void throwIf(boolean condition,RuntimeException exception){
		if(condition){
			throw exception;
		}
	}

	/**
	 * 条件成立抛出异常
	 * @param condition
	 * @param errorCode
	 */
	public static void throwIf(boolean condition, ErrorCode errorCode){
		throwIf(condition,new BusinessException(errorCode));
	}

	/**
	 * 条件成立抛出异常
	 * @param condition
	 * @param errorCode
	 * @param message 异常信息
	 */
	public static void throwIf(boolean condition,ErrorCode errorCode,String message){
		throwIf(condition,new BusinessException(errorCode,message));
	}

}
