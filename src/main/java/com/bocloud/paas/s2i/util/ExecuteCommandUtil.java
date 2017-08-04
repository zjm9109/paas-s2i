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
			printMessage(ps.getInputStream());
			printMessage(ps.getErrorStream());
			int value = ps.waitFor();
			result.setSuccess(true);
			result.setCode(value);
			result.setMessage("");
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

	private static void printMessage(final InputStream input) {
		new Thread(new Runnable() {
			public void run() {
		    	Reader reader = new InputStreamReader(input);
		        BufferedReader bf = new BufferedReader(reader);
		        String line = null;
		        try {
		        	while((line=bf.readLine())!=null) {
						logger.info(line);
		            }
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }
			}
	    }).start();
	}

}
