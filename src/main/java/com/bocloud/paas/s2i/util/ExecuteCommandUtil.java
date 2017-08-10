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
			getResult(ps.getInputStream());
			getResult(ps.getErrorStream());
			int value = ps.waitFor();
			result.setSuccess(true);
			result.setCode(value);
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
	private static void getResult(final InputStream input) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Reader reader = new InputStreamReader(input);
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				try {
					while((line=bf.readLine())!=null) {
						logger.info(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();
		//		String result = "";
//		ExecutorService executorService = Executors.newCachedThreadPool();
//		Callable<String> callable = new Callable<String>() {
//			@Override
//			public String call() throws Exception {
//				StringBuffer execResult = new StringBuffer();
//				Reader reader = new InputStreamReader(input);
//				BufferedReader bf = new BufferedReader(reader);
//				String line = null;
//				try {
//					while ((line = bf.readLine()) != null) {
//						logger.info(line);
//						execResult.append(line).append("\n");
//					}
//				} catch (IOException e) {
//					logger.error("get result exec error:\n", e);
//				} finally {
//					FileUtil.closeStream(bf);
//					reader.close();
//				}
//				logger.info("call里面执行的～～～～～～～～～～～～～～～～" + execResult.toString());
//				return execResult.toString();
//			}
//		};
//		Future<String> future = executorService.submit(callable);
//		try {
//			logger.info("getResult里面执行的～～～～～～～～～～～～～～～～" + future.get());
//			if (future.isDone()) {
//				result = future.get();
//			}
//		} catch (Exception e) {
//			logger.error("get result exec error:\n", e);
//		}
//		
//		return result;
	}

}
