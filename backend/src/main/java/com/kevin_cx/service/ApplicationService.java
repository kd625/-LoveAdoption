package com.kevin_cx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin_cx.model.entity.Application;

/**
* @author Zhuanz
* @description 针对表【application】的数据库操作Service
* @createDate 2025-06-01 22:17:47
*/
public interface ApplicationService extends IService<Application> {

	/**
	 * 参数校验
	 * @param application
	 * @param b
	 */
	void validApplication(Application application, boolean b);
}
