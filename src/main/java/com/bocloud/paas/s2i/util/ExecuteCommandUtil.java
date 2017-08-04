package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteCommandUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandUtil.class);

	public static Result exec(String[] commands) {
		Result result = new Result(false, "");
		Process ps = null;
		BufferedReader br = null;
		String cmd = "";
		try {
			for (String command : commands) {
				cmd += command + " ";
			}
			logger.info("——————————————————————————————————> start execute [" + cmd + "]...");
			ps = Runtime.getRuntime().exec(commands);
			ps.waitFor();
			int code = ps.exitValue();
			// 方法阻塞, 等待命令执行完成（成功会返回0）
			if (code == 0) {
				br = getResult(ps.getInputStream());
			} else {
				br = getResult(ps.getErrorStream());
			}
			StringBuffer execResult = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				execResult.append(line).append("\n");
			}
			result.setSuccess(true);
			result.setCode(code);
			result.setMessage(execResult.toString());
		} catch (Exception e) {
			logger.error("——————————————————————————————————> execute [" + cmd + "] fail: \n" + e);
		} finally {
			FileUtil.closeStream(br);
			// 销毁子进程
			if (ps != null) {
				ps.destroy();
			}
		}
		return result;
	}

	public static BufferedReader getResult(final InputStream input) {
		StringBuffer execResult = new StringBuffer();
		new Thread (new Runnable()  {
			@Override
			public void run() {
				try {
					Reader reader = new InputStreamReader(input, "UTF-8");
					BufferedReader bf = new BufferedReader(reader);
					String line = null;
					while ((line = bf.readLine()) != null) {
						execResult.append(line).append("\n");
					}
				} catch (Exception e) {
					logger.error("get result fail: \n", e);
				}
			}
		}).start();

		return null;
	}

}
