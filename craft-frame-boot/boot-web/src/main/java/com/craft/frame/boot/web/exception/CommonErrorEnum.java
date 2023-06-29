package com.craft.frame.boot.web.exception;

/**
 * @author DURR
 * @desc 通用错误码
 * @date 2023/6/25 20:16
 */
public enum CommonErrorEnum implements ErrorEnum {

    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试哦~~"),

    PARAM_VALID(-2, "参数校验失败"),

    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),

    NO_ACCESS(-4, "不允许访问"),
            ;

    private final Integer code;
    private final String msg;

    CommonErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
