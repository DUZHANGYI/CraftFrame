package com.craft.frame.commons.lang.res;

/**
 * @author DURR
 * @desc 基础返回结果类型
 * @date 2023/6/23 20:32
 */
public abstract class Result<R> {

	private boolean success;

	private Code code;

	private R result;

	protected Result(boolean success, Code code, R result) {
		this.success = success;
		this.code = code;
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public Code getCode() {
		return code;
	}

	public R getResult() {
		return result;
	}

	protected void setSuccess(boolean success) {
		this.success = success;
	}

	protected void setCode(Code code) {
		this.code = code;
	}

	protected void setResult(R result) {
		this.result = result;
	}

}
