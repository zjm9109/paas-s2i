package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteCommandUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandUtil.class);

	public static Result exec(String[] commands, String fileName) {
		Result result = new Result(false, "");
		Process ps = null;
		String cmd = "";
		try {
			for (String command : commands) {
				cmd += command + " ";
			}
			logger.info("——————————————————————————————————> start execute [" + cmd + "]...");
			ps = Runtime.getRuntime().exec(commands);
			getResult(ps.getInputStream(), fileName);
			getResult(ps.getErrorStream(), fileName);
			int value = ps.waitFor();
			result.setSuccess(true);
			result.setCode(value);
		} catch (Exception e) {
			logger.error("——————————————————————————————————> execute [" + cmd + "] fail: \n" + e);
		} finally {
			// 销毁子进程
			if (ps != null) {
				ps.destroy();
			}
		}
		return result;
	}

	/**
	 * 获取输出结果
	 * 
	 * @param input
	 * @return
	 */
	private static void getResult(final InputStream input, String fileName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Reader reader = new InputStreamReader(input);
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				StringBuffer execResult = new StringBuffer();
				try {
					while ((line = bf.readLine()) != null) {
						logger.info(line);
						execResult.append(line).append("\n");
					}
					if (!FileUtil.createFile(fileName, execResult.toString())) {
						logger.warn("——————————————————————————————————> save the build result fail to the [" + fileName
								+ "] fail！");
					}
				} catch (IOException e) {
					logger.error("get result exec error:\n", e);
				} finally {
					FileUtil.closeStream(bf);
				}
			}

		}).start();
	}

}
