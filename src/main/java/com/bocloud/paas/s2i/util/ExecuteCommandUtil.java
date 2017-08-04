package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
			String isMessage = getResult(ps.getInputStream());
			String esMssage = getResult(ps.getErrorStream());
			int value = ps.waitFor();
			result.setSuccess(true);
			result.setCode(value);
			if (value == 0) {
				result.setMessage(isMessage);
			} else {
				result.setMessage(esMssage);
			}
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
	
	/**
	 * 获取输出结果
	 * @param input
	 * @return
	 */
	private static String getResult(final InputStream input) {
		String result = "";
		ExecutorService executorService = Executors.newCachedThreadPool();
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				StringBuffer execResult = new StringBuffer();
				Reader reader = new InputStreamReader(input);
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				try {
					while ((line = bf.readLine()) != null) {
						execResult.append(line).append("\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				logger.info("call里面执行的～～～～～～～～～～～～～～～～" + execResult.toString());
				return execResult.toString();
			}
		};
		Future<String> future = executorService.submit(callable);
		try {
			logger.info("getResult里面执行的～～～～～～～～～～～～～～～～" + future.get());
			if (future.isDone()) {
				result = future.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
