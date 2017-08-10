package com.bocloud.paas.s2i.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bocloud.paas.s2i.service.STIServiceImpl;
import com.bocloud.paas.s2i.util.Result;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private STIServiceImpl stiService;

	/**
	 * 构建s2i镜像
	 * 
	 * @param baseImage
	 *            基础镜像名称
	 * @param repositoryUrl
	 *            代码仓库地址
	 * @param repositoryBranch
	 *            代码仓库分支，默认master
	 * @param repositoryUsername
	 *            代码仓库用户名
	 * @param repositoryPassword
	 *            代码仓库密码
	 * @param warName
	 *            war包名称
	 * @param newImage
	 *            构建后的镜像名称
	 */
	@RequestMapping(value = "/build", method = { RequestMethod.GET })
	public Result build(String baseImage, String repositoryUrl, String repositoryBranch, String repositoryUsername,
			String repositoryPassword, String warName, String newImage) {
		return stiService.build(baseImage, repositoryUrl, repositoryBranch, repositoryUsername, repositoryPassword, warName,
				newImage);
	}
}
