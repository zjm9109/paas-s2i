package com.bocloud.paas.s2i.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bocloud.paas.s2i.util.FileUtil;

/**
 * s2i服务类
 * 
 * @author zjm
 *
 */
@Service("STIService")
public class STIServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(STIServiceImpl.class);
	/**
	 * s2i存放结果文件主路径
	 */
	private static final String STI_HOME = "/opt/s2i_home/";
	/**
	 * s2i构建脚本的文件名
	 */
	private static final String BUILD_SH_NAME = "build.sh";
	
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
	public void build(String baseImage, String repositoryUrl, String repositoryBranch, String repositoryUsername,
			String repositoryPassword, String warName, String newImage) {
		StringBuffer command = new StringBuffer();
		command.append("sh ").append(STI_HOME).append("shell/").append(BUILD_SH_NAME).append(" ");
		command.append(warName).append(" ").append(repositoryUrl).append(" ").append(baseImage);
		command.append(" ").append(newImage).append(" ").append(repositoryBranch);
		logger.info("——————————————————————————————————> s2i build command: " + command.toString());
		Process ps = null;
		BufferedReader br = null;
		try {
			logger.info("——————————————————————————————————> start execute s2i build...");
			ps = Runtime.getRuntime().exec(command.toString());
			int code = ps.waitFor();
			// 方法阻塞, 等待命令执行完成（成功会返回0）
			if (code == 0) {
				br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(ps.getErrorStream(), "UTF-8"));
			}
            StringBuffer result = new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                result.append(line).append("\n");  
            }  
            // 将构建镜像的结果保存在文件中
            newImage = newImage.lastIndexOf("/") > 0 ? newImage.substring(newImage.lastIndexOf("/")) : newImage;
            newImage = newImage.lastIndexOf(":") > 0 ? newImage.replace(":", "_") : newImage;
            String fileName = newImage + "-" + Long.toString(System.currentTimeMillis() / 1000);
            fileName = STI_HOME + "build/" + fileName;
            if (code == 0) {
            	logger.info("——————————————————————————————————> execute s2i build success: \n" + result);
            } else {
            	logger.error("——————————————————————————————————> execute s2i build fail: \n" + result);
            }
            if (!FileUtil.createFile(fileName, result.toString())) {
            	logger.warn("——————————————————————————————————> save the build result fail to the " + fileName + " fail！");
            }
		} catch (Exception e) {
			logger.error("——————————————————————————————————> execute s2i build fail: \n" + e);
		} finally {
			FileUtil.closeStream(br);

            // 销毁子进程
            if (ps != null) {
                ps.destroy();
            }
		}
	}
}
