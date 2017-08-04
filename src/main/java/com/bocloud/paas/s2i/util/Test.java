package com.bocloud.paas.s2i.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//用两个线程来分别获取标准输出流和错误输出流,否则会造成io阻塞导致程序卡死
public class Test {
	

	public static String execute(String[] cmd) throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer();

		final Process p = Runtime.getRuntime().exec(cmd);

		/* 为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞 */
		Thread t = new Thread(new InputStreamRunnable(p.getErrorStream(), "ErrorStream"));
		t.start();

		BufferedReader reader = null;
		InputStream output = p.getInputStream();
		// InputStream error = p.getErrorStream();
		String line = "";

		reader = new BufferedReader(new InputStreamReader(output));
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		p.waitFor();
		// if (0x00 == sb.length()) {
		// reader = new BufferedReader(new InputStreamReader(error));
		//
		// while ((line = reader.readLine())!= null) {
		// sb.append(line + "\n");
		// }
		// }
		reader.close();
		p.destroy();
		return sb.toString();
	}
}

/** 读取InputStream的线程 */
class InputStreamRunnable implements Runnable {
	BufferedReader bReader = null;
	String type = null;

	public InputStreamRunnable(InputStream is, String _type) {
		try {
			bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
			type = _type;
		} catch (Exception ex) {
		}
	}

	public void run() {
		String line;
		int lineNum = 0;

		try {
			while ((line = bReader.readLine()) != null) {
				lineNum++;
				// Thread.sleep(200);
			}
			bReader.close();
		} catch (Exception ex) {
		}
	}
}