package com.kevin_cx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin_cx.annotation.AuthCheck;
import com.kevin_cx.common.BaseResponse;
import com.kevin_cx.common.DeleteRequest;
import com.kevin_cx.common.ErrorCode;
import com.kevin_cx.common.ResultUtils;
import com.kevin_cx.constant.CommonConstant;
import com.kevin_cx.exception.BusinessException;
import com.kevin_cx.model.dto.application.ApplicationAddRequest;
import com.kevin_cx.model.dto.application.ApplicationQueryRequest;
import com.kevin_cx.model.dto.application.ApplicationUpdateRequest;
import com.kevin_cx.model.entity.Application;
import com.kevin_cx.model.entity.User;
import com.kevin_cx.service.ApplicationService;
import com.kevin_cx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author kevinCx
 */
@RestController
@RequestMapping("/application")
@Slf4j
public class ApplicationController {

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserService userService;

	// region 增删改查

	/**
	 * 创建
	 *
	 * @param applicationAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addApplication(@RequestBody ApplicationAddRequest applicationAddRequest, HttpServletRequest request) {
		if (applicationAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Application application = new Application();
		BeanUtils.copyProperties(applicationAddRequest, application);
		// 校验
		User loginUser = userService.getLoginUser(request);
		application.setUserId(loginUser.getId());
		boolean result = applicationService.save(application);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		long newApplicationId = application.getId();
		return ResultUtils.success(newApplicationId);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteApplication(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		Application oldApplication = applicationService.getById(id);
		if (oldApplication == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可删除
		if (!oldApplication.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = applicationService.removeById(id);
		return ResultUtils.success(b);
	}

	/**
	 * 更新
	 *
	 * @param applicationUpdateRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/update")
	public BaseResponse<Boolean> updateApplication(@RequestBody ApplicationUpdateRequest applicationUpdateRequest,
											HttpServletRequest request) {
		if (applicationUpdateRequest == null || applicationUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Application application = new Application();
		BeanUtils.copyProperties(applicationUpdateRequest, application);
		// 参数校验
		//applicationService.validApplication(application, false);
		User user = userService.getLoginUser(request);
		long id = applicationUpdateRequest.getId();
		// 判断是否存在
		Application oldApplication = applicationService.getById(id);
		if (oldApplication == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可修改
		if (!oldApplication.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = applicationService.updateById(application);
		return ResultUtils.success(result);
	}

	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public BaseResponse<Application> getApplicationById(long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Application application = applicationService.getById(id);
		return ResultUtils.success(application);
	}

	/**
	 * 获取列表（仅管理员可使用）
	 *
	 * @param applicationQueryRequest
	 * @return
	 */
	@AuthCheck(mustRole = "admin")
	@GetMapping("/list")
	public BaseResponse<List<Application>> listApplication(ApplicationQueryRequest applicationQueryRequest) {
		Application applicationQuery = new Application();
		if (applicationQueryRequest != null) {
			BeanUtils.copyProperties(applicationQueryRequest, applicationQuery);
		}
		QueryWrapper<Application> queryWrapper = new QueryWrapper<>(applicationQuery);
		List<Application> applicationList = applicationService.list(queryWrapper);
		return ResultUtils.success(applicationList);
	}

	/**
	 * 分页获取列表
	 *
	 * @param applicationQueryRequest
	 * @param request
	 * @return
	 */
	@GetMapping("/list/page")
	public BaseResponse<Page<Application>> listApplicationByPage(ApplicationQueryRequest applicationQueryRequest, HttpServletRequest request) {
		if (applicationQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Application applicationQuery = new Application();
		BeanUtils.copyProperties(applicationQueryRequest, applicationQuery);
		long current = applicationQueryRequest.getCurrent();
		long size = applicationQueryRequest.getPageSize();
		String sortField = applicationQueryRequest.getSortField();
		String sortOrder = applicationQueryRequest.getSortOrder();
		String content = applicationQuery.getApplication_reason();
		// content 需支持模糊搜索
		applicationQuery.setApplication_reason(null);
		// 限制爬虫
		if (size > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		QueryWrapper<Application> queryWrapper = new QueryWrapper<>(applicationQuery);
		queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
				sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
		Page<Application> applicationPage = applicationService.page(new Page<>(current, size), queryWrapper);
		return ResultUtils.success(applicationPage);
	}

	// endregion

}
