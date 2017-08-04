package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

//用两个线程来分别获取标准输出流和错误输出流,否则会造成io阻塞导致程序卡死
public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		String[] cmd = {"/usr/local/bin/docker", "pull", "java"};
		final Process process = Runtime.getRuntime().exec(cmd);
		printMessage(process.getInputStream());
		printMessage(process.getErrorStream());
		int value = process.waitFor();
		System.out.println(value);
	}

	private static void printMessage(final InputStream input) {
		new Thread(new Runnable() {
			public void run() {
		    	Reader reader = new InputStreamReader(input);
		        BufferedReader bf = new BufferedReader(reader);
		        String line = null;
		        try {
		        	while((line=bf.readLine())!=null) {
		        		System.out.println(line);
		            }
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }
			}
	    }).start();
	}
}