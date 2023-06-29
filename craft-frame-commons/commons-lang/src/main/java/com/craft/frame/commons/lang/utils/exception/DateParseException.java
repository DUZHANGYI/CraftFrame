package com.craft.frame.commons.lang.utils.exception;

/**
 * @author DURR
 * @desc 日期格式化错误异常
 * @date 2023/6/29 13:20
 */
public class DateParseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DateParseException(Throwable e) {
		super(e);
	}

}
