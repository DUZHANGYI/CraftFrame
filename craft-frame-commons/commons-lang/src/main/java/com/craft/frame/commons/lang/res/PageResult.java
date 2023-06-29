package com.craft.frame.commons.lang.res;

import java.util.List;

/**
 * @author DURR
 * @desc 分页处理所使用的返回结果
 * @date 2023/6/23 20:37
 */
public class PageResult<R> extends Result<List<R>> {

	/**
	 * 总数
	 */
	protected long total = 0;
	/**
	 * 每页显示条数，默认 10
	 */
	protected long size = 10;
	/**
	 * 当前页
	 */
	protected long current = 1;

	private PageResult(boolean success, Code code, long total, long size, long current, List<R> result) {
		super(success, code, result);
		this.total = total;
		this.size = size;
		this.current = current;
	}

	public static <RES> PageResult<RES> succ(long total, long size, long current, List<RES> r) {
		return new PageResult<>(true, Code.CODE_SUCC, total, size, current, r);
	}

	public static <RES> PageResult<RES> fail() {
		return new PageResult<>(false, Code.CODE_ERROR_UNKNOWN, 0L, 10L, 1L, null);
	}

	public static <RES> PageResult<RES> fail(String errMsg) {
		return new PageResult<>(false, new Code(Code.CODE_ERROR_UNKNOWN.getMsgCode(), errMsg), 0L, 10L, 1L, null);
	}

	public static <RES> PageResult<RES> fail(Code code) {
		if (code == null) {
			code = Code.CODE_ERROR_UNKNOWN;
		}
		return new PageResult<>(false, code, 0L, 10L, 1L, null);
	}

	public static <RES> PageResult<RES> fail(Throwable e) {
		return new PageResult<>(false, new Code(e), 0L, 10L, 1L, null);
	}

	public static <RES> PageResult<RES> fail(String code, Throwable e) {
		return new PageResult<>(false, new Code(code, e), 0L, 10L, 1L, null);
	}

	public static <RES> PageResult<RES> fail(Result<?> f) {
		return new PageResult<>(false, f.getCode(), 0L, 10L, 1L, null);
	}

	public long getTotal() {
		return total;
	}

	public long getSize() {
		return size;
	}

	public long getCurrent() {
		return current;
	}

}
