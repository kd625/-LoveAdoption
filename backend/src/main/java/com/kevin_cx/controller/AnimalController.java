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
import com.kevin_cx.model.dto.animal.AnimalAddRequest;
import com.kevin_cx.model.dto.animal.AnimalQueryRequest;
import com.kevin_cx.model.dto.animal.AnimalUpdateRequest;
import com.kevin_cx.model.entity.Animal;
import com.kevin_cx.model.entity.User;
import com.kevin_cx.service.AnimalService;
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
@RequestMapping("/animal")
@Slf4j
public class AnimalController {

	@Resource
	private AnimalService animalService;

	@Resource
	private UserService userService;

	// region 增删改查

	/**
	 * 创建
	 *
	 * @param animalAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addAnimal(@RequestBody AnimalAddRequest animalAddRequest, HttpServletRequest request) {
		if (animalAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Animal animal = new Animal();
		BeanUtils.copyProperties(animalAddRequest, animal);
		// 校验
		//animalService.validAnimal(animal, true);
		User loginUser = userService.getLoginUser(request);
		animal.setUserId(loginUser.getId());
		boolean result = animalService.save(animal);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		long newAnimalId = animal.getId();
		return ResultUtils.success(newAnimalId);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteAnimal(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		Animal oldAnimal = animalService.getById(id);
		if (oldAnimal == null||oldAnimal.getUserId()==null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		boolean test = !oldAnimal.getUserId().equals(user.getId()) && !userService.isAdmin(request);
		System.out.println(test);
		// 仅本人或管理员可删除
		if (!oldAnimal.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = animalService.removeById(id);
		return ResultUtils.success(b);
	}

	/**
	 * 更新
	 *
	 * @param animalUpdateRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/update")
	public BaseResponse<Boolean> updateAnimal(@RequestBody AnimalUpdateRequest animalUpdateRequest,
											  HttpServletRequest request) {
		if (animalUpdateRequest == null || animalUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Animal animal = new Animal();
		BeanUtils.copyProperties(animalUpdateRequest, animal);
		// 参数校验
		//animalService.validAnimal(animal, false);
		User user = userService.getLoginUser(request);
		long id = animalUpdateRequest.getId();
		// 判断是否存在
		Animal oldAnimal = animalService.getById(id);
		if (oldAnimal == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可修改
//		boolean test = userService.isAdmin(request);
//		if (!oldAnimal.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//		}
		boolean result = animalService.updateById(animal);
		return ResultUtils.success(result);
	}

	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public BaseResponse<Animal> getAnimalById(long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Animal animal = animalService.getById(id);
		return ResultUtils.success(animal);
	}

	/**
	 * 获取列表（仅管理员可使用）
	 *
	 * @param animalQueryRequest
	 * @return
	 */
	@AuthCheck(mustRole = "admin")
	@GetMapping("/list")
	public BaseResponse<List<Animal>> listAnimal(AnimalQueryRequest animalQueryRequest) {
		Animal animalQuery = new Animal();
		if (animalQueryRequest != null) {
			BeanUtils.copyProperties(animalQueryRequest, animalQuery);
		}
		QueryWrapper<Animal> queryWrapper = new QueryWrapper<>(animalQuery);
		List<Animal> animalList = animalService.list(queryWrapper);
		return ResultUtils.success(animalList);
	}

	/**
	 * 分页获取列表
	 *
	 * @param animalQueryRequest
	 * @param request
	 * @return
	 */
	@GetMapping("/list/page")
	public BaseResponse<Page<Animal>> listAnimalByPage(AnimalQueryRequest animalQueryRequest, HttpServletRequest request) {
		if (animalQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Animal animalQuery = new Animal();
		BeanUtils.copyProperties(animalQueryRequest, animalQuery);
		long current = animalQueryRequest.getCurrent();
		long size = animalQueryRequest.getPageSize();
		String sortField = animalQueryRequest.getSortField();
		String sortOrder = animalQueryRequest.getSortOrder();

		// 限制爬虫
		if (size > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		QueryWrapper<Animal> queryWrapper = new QueryWrapper<>(animalQuery);
		// queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
				sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
		Page<Animal> animalPage = animalService.page(new Page<>(current, size), queryWrapper);
		return ResultUtils.success(animalPage);
	}

	// endregion

}
