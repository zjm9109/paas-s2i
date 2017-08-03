package com.bocloud.paas.s2i.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteCommandUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandUtil.class);
	
	public static Result exec(String command) {
		Result result = new Result(false, "");
		Process ps = null;
		BufferedReader br = null;
		try {
			logger.info("——————————————————————————————————> start execute [" + command + "]...");
			ps = Runtime.getRuntime().exec(command.toString());
			ps.waitFor();
			int code = ps.exitValue();
			// 方法阻塞, 等待命令执行完成（成功会返回0）
			if (code == 0) {
				br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(ps.getErrorStream(), "UTF-8"));
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
			logger.error("——————————————————————————————————> execute [" 
					+ command + "] fail: \n" + e);
		} finally {
			FileUtil.closeStream(br);
            // 销毁子进程
            if (ps != null) {
                ps.destroy();
            }
		}
		return result;
		
	}
}
