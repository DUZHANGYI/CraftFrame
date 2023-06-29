package com.craft.frame.commons.lang.res;

import org.apache.commons.lang3.StringUtils;

/**
 * @author DURR
 * @desc 业务状态码
 * @date 2023/6/23 20:26
 */
public class Code {

	public static String MSG_PARAM_PLACEHOLDER = "{}";

	public static Code CODE_SUCC = new Code("success", "执行成功");
	public static Code CODE_ERROR = new Code("error", "执行失败");

	public static Code CODE_ERROR_UNKNOWN = new Code("error.unknown", "未知错误");

	public static Code CODE_ERROR_INTERNAL = new Code("error.internal", "内部错误");

	public static Code CODE_ERROR_PARAMS = new Code("error.params", "无效的参数");

	public static Code CODE_ERROR_UN_SUPPORT = new Code("error.unsupport", "暂不支持");

	public final Throwable THROWABLE_NO_ERROR = new Throwable("no error occur");

	private final String msgCode;

	private String msgDefault;

	private final ThreadLocal<String[]> msgParams = new ThreadLocal<>();

	private final Throwable error;

	private boolean hasError = true;

	static {
		CODE_SUCC.hasError = false;
	}

	public Code(String msgCode) {
		this(msgCode, null, null);
	}

	public Code(Throwable error) {
		this(CODE_ERROR_INTERNAL.getMsgCode(), error.getMessage(), error);
	}

	public Code(String msgCode, Throwable error) {
		this(msgCode, error.getMessage(), error);
	}

	public Code(String msgCode, String msgDefault) {
		this(msgCode, msgDefault, null);
	}

	public Code(String msgCode, String msgDefault, Throwable error) {
		this.msgCode = msgCode;
		this.msgDefault = msgDefault;
		this.error = error;
		if (StringUtils.isBlank(this.msgDefault) && error != null) {
			this.msgDefault = error.getMessage();
		}
	}

	Code setMsgParams(String... params) {
		this.msgParams.set(params);
		return this;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public String getMsgDefault() {
		if (StringUtils.isBlank(msgDefault) && this.error != null) {
			return error.getMessage();
		}
		String msg = String.format(msgDefault.replace(MSG_PARAM_PLACEHOLDER, "%s"), (Object[]) this.msgParams.get());
		msgParams.remove();
		return msg;
	}

	public Throwable getError() {
		if (this.error != null) {
			return error;
		}
		if (!hasError) {
			return THROWABLE_NO_ERROR;
		}
		if (StringUtils.isNotBlank(this.getMsgDefault())) {
			return new Throwable(this.getMsgDefault());
		}
		return new Throwable("unknown error");
	}

}
