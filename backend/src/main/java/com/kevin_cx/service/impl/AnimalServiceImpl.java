package com.kevin_cx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin_cx.common.ErrorCode;
import com.kevin_cx.exception.BusinessException;
import com.kevin_cx.model.entity.Animal;
import com.kevin_cx.model.enums.PostGenderEnum;
import com.kevin_cx.model.enums.PostReviewStatusEnum;
import com.kevin_cx.service.AnimalService;
import com.kevin_cx.mapper.AnimalMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Zhuanz
* @description 针对表【animal】的数据库操作Service实现
* @createDate 2025-06-01 22:15:37
*/
@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal>
    implements AnimalService{


}




