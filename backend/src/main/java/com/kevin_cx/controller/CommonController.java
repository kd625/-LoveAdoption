package com.kevin_cx.controller;


import com.kevin_cx.common.BaseResponse;
import com.kevin_cx.common.ErrorCode;
import com.kevin_cx.common.ResultUtils;
import com.kevin_cx.util.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

	@Resource
	private AliOssUtil aliOssUtil;

	/**
	 * 文件上传
	 * @param file
	 * @return
	 */

	@PostMapping("/upload")
	@ApiOperation("文件上传")
	public BaseResponse<String> upload(@RequestPart("file") MultipartFile file){
		log.info("文件上传：{}",file);

		try {
			//原始文件名
			String originalFilename = file.getOriginalFilename();
			//截取原始文件名的后缀   dfdfdf.png
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			//构造新文件名称
			String objectName = UUID.randomUUID().toString() + extension;

			//文件的请求路径
			String filePath = aliOssUtil.upload(file.getBytes(), objectName);
			return ResultUtils.success(filePath);
		} catch (IOException e) {
			log.error("文件上传失败：{}", e);
		}

		return ResultUtils.error(ErrorCode.OPERATION_ERROR);
	}

}
