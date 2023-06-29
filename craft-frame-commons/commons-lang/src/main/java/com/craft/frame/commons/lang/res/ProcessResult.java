package com.craft.frame.commons.lang.res;

/**
 * @author DURR
 * @desc 逻辑处理所使用的返回结果
 * @date 2023/6/23 20:34
 */

public class ProcessResult<R> extends Result<R> {

	protected ProcessResult(boolean success, Code code, R result) {
		super(success, code, result);
	}

	public static <RES> ProcessResult<RES> succ() {
		return new ProcessResult<>(true, Code.CODE_SUCC, null);
	}

	public static <RES> ProcessResult<RES> succ(RES r) {
		return new ProcessResult<>(true, Code.CODE_SUCC, r);
	}

	public static <RES> ProcessResult<RES> fail() {
		return new ProcessResult<>(false, Code.CODE_ERROR_UNKNOWN, null);
	}

	public static <RES> ProcessResult<RES> fail(String errMsg) {
		return new ProcessResult<>(false, new Code(Code.CODE_ERROR_UNKNOWN.getMsgCode(), errMsg), null);
	}

	public static <RES> ProcessResult<RES> fail(Code code) {
		return fail(code, (String[])null);
	}

	public static <RES> ProcessResult<RES> fail(Code code, String params) {
		return fail(code, new String[] { params });
	}

	public static <RES> ProcessResult<RES> fail(Code code, String... params) {
		if (code == null) {
			code = Code.CODE_ERROR_UNKNOWN;
		}
		return new ProcessResult<>(false, code.setMsgParams(params), null);
	}

	public static <RES> ProcessResult<RES> fail(Code code, RES res) {
		if (code == null) {
			code = Code.CODE_ERROR_UNKNOWN;
		}
		return new ProcessResult<>(false, code, res);
	}

	public static <RES> ProcessResult<RES> fail(Throwable e) {
		return new ProcessResult<>(false, new Code(e), null);
	}

	public static <RES> ProcessResult<RES> fail(String code, Throwable e) {
		return new ProcessResult<>(false, new Code(code, e), null);
	}

	public static <RES> ProcessResult<RES> fail(Result<?> f) {
		return new ProcessResult<>(false, f.getCode(), null);
	}
}
