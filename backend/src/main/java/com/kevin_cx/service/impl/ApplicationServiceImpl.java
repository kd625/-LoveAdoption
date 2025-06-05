package com.kevin_cx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin_cx.common.ErrorCode;
import com.kevin_cx.exception.ThrowUtils;
import com.kevin_cx.model.entity.Application;
import com.kevin_cx.service.ApplicationService;
import com.kevin_cx.mapper.ApplicationMapper;
import org.springframework.stereotype.Service;

/**
* @author Zhuanz
* @description 针对表【application】的数据库操作Service实现
* @createDate 2025-06-01 22:17:47
*/
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application>
    implements ApplicationService{

	@Override
	public void validApplication(Application application, boolean b) {
		ThrowUtils.throwIf(application == null, ErrorCode.PARAMS_ERROR);
	}
}




