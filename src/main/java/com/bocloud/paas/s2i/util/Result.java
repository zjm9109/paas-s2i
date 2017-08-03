package com.bocloud.paas.s2i.util;

public class Result {

	private int code = -1;
	private String message;
	private Object data;
	private boolean success = false;
	
	public Result() {
		
	}
	
	public Result(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public Result(boolean success, int code, String message, Object data) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
