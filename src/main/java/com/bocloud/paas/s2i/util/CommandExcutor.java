package com.bocloud.paas.s2i.util;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSH客户端脚本执行器
 * 
 * @author dmw
 *
 */
public class CommandExcutor {

	final static String DEFAULT_USER = "root";

	final static Logger logger = LoggerFactory.getLogger(CommandExcutor.class);

	public static SSH getSsh(String address, String password) {
		return new SSH(address, DEFAULT_USER, password);
	}

	public static SSH getSsh(String address, String username, String password) {
		return new SSH(address, username, password);
	}

	public static boolean execute(SSH ssh, String command) {
		if (null == ssh) {
			logger.error("SSH client is null");
			return false;
		}
		try {
			if (!ssh.connect()) {
				return false;
			}
		} catch (IOException e1) {
			logger.error("Create SSH connection whith command:[{}] error", command, e1);
			return false;
		}
		try {
			return ssh.execute(command);
		} catch (Exception e) {
			logger.error("Execute command:[{}] error", command, e);
			return false;
		} finally {
			ssh.close();
		}

	}

	public static String execute4Result(SSH ssh, String command) {
		if (null == ssh) {
			logger.error("SSH client is null");
			return null;
		}
		try {
			if (!ssh.connect()) {
				return null;
			}
		} catch (IOException e1) {
			logger.error("Create SSH connection whith command:[{}] error", command, e1);
			return null;
		}
		try {
			return ssh.executeWithResult(command, 60 * 1000);
		} catch (Exception e) {
			logger.error("Execute command:[{}] error", command, e);
			return null;
		} finally {
			ssh.close();
		}
	}

}
