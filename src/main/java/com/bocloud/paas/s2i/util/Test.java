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

//用两个线程来分别获取标准输出流和错误输出流,否则会造成io阻塞导致程序卡死
public class Test {
	
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		String[] cmd = {"/usr/local/bin/docker", "pull", "java"};
		final Process process = Runtime.getRuntime().exec(cmd);
		printMessage(process.getInputStream());
		printMessage(process.getErrorStream());
		int value = process.waitFor();
		System.out.println(value);
	}

	private static String printMessage(final InputStream input) {
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
						logger.info(line);
						execResult.append(line).append("\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return execResult.toString();
			}
		};
		Future<String> future = executorService.submit(callable);
		try {
			if (future.isDone()) {
				result = future.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}