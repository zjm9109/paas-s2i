package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 递归删除目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (null == children) {
				return dir.delete();
			}
			// 递归删除目录中的子目录
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param hostIp
	 *            主机ip
	 * @param hostUser
	 *            主机用户
	 * @param hostPwd
	 *            主机密码
	 * @param folder
	 *            需要创建的文件夹
	 * @return
	 */
	public static boolean mkdirFolder(String hostIp, String hostUser, String hostPwd, String folder) {
		SSH ssh = CommandExcutor.getSsh(hostIp, hostUser, hostPwd);
		try {
			if (ssh.connect()) {
				String command = "cd; mkdir -p ~/" + folder + "; cd ~/" + folder + "; pwd";
				try {
					String result = ssh.executeWithResult(command);
					logger.info(result);
					return true;
				} catch (Exception e) {
					logger.error("创建文件夹异常：", e);
					return false;
				}
			}
			logger.error("SSH无法连接目标主机(" + hostIp + ")！请检查主机运行状态、网络连接状态或核对输入正确的用户名和密码。");
			return false;
		} catch (NullPointerException | IOException npe) {
			logger.error("Get host connection exception：", npe);
			return false;
		} finally {
			ssh.close();
		}
	}

	/**
	 * 文件创建 并写入内容
	 * 
	 * @param fileName
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(String fileName, String content) {
		File file = new File(fileName);
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				logger.error("创建目标文件所在目录失败！");
				return false;
			}
		}

		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			logger.error("文件内容写入失败: ", e);
			return false;
		}
		logger.info("文件内容写入成功！");
		return true;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean readFile(String filePath) {
		StringBuffer content = new StringBuffer();
		String encoding = "UTF-8";
		File file = new File(filePath);
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				content.append(lineTxt).append("\n");
			}
			bufferedReader.close();
			read.close();
		} catch (IOException e) {
			logger.error("Read file contents exception：", e);
			return false;
		}
		logger.info("读取文件成功！");
		return true;
	}

	/**
	 * 获取文件路径
	 * 
	 * @param fileDir
	 *            文件在resource里的文件目录
	 * @return
	 */
	public static String filePath(String fileDir) {
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("") + fileDir + File.separatorChar;
			// 判断操作系统类型
			if (System.getProperty("os.name").toUpperCase().contains("windows".toUpperCase())) {
				path = path.replace("file:/", "");
			} else {
				path = path.replace("file:", "");
			}
			return path;
		} catch (Exception e) {
			logger.error("Get fileDir [" + fileDir + "] path exception：", e);
			return "";
		}

	}

}
