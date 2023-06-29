package com.craft.frame.boot.web.response;

import com.craft.frame.commons.lang.res.Code;
import com.craft.frame.commons.lang.res.ProcessResult;
import org.springframework.http.HttpStatus;

/**
 * @author DURR
 * @desc 操作类型
 * @date 2023/6/23 22:18
 */
public class Operation<T> extends SingleData<T> {
    public Operation() {
        this.setType(ResponseType.OPER);
    }

    private Operation(boolean succ, T data, String... message) {
        this();
        this.setCode(succ ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());
        this.setSuccess(succ);
        this.setData(data);
        this.setMessage(message);
    }

    private Operation(Integer code, boolean succ, T data, String... message) {
        this();
        this.setCode(code);
        this.setSuccess(succ);
        this.setData(data);
        this.setMessage(message);
    }

    public static Operation<?> succ(String... message) {
        return new Operation<>(true, null, message);
    }

    public static <O> Operation<O> succ(O data) {
        return new Operation<>(true, data, (String) null);
    }

    public static <O> Operation<O> succ(O data, String... message) {
        return new Operation<>(true, data, message);
    }

    public static Operation<?> succI18N(String defaultMsg, String msgCode, Object... msgParams) {
        Operation<?> oper = succ();
        oper.translate(HttpStatus.OK.value(), defaultMsg, msgCode, msgParams);
        return oper;
    }

    public static Operation<?> succI18N(Integer code, Code msg, Object... msgParams) {
        Operation<?> oper = succ();
        if (code != null) {
            oper.translate(code, msg.getMsgDefault(), msg.getMsgCode(), msgParams);
        }

        return oper;
    }

    public static <O> Operation<O> succI18N(O data, String defaultMsg, String msgCode, Object... msgParams) {
        Operation<O> oper = succ(data);
        oper.translate(HttpStatus.OK.value(), defaultMsg, msgCode, msgParams);
        return oper;
    }

    public static <O> Operation<O> succI18N(O data, Code code, Object... msgParams) {
        Operation<O> oper = succ(data);
        if (code != null) {
            oper.translate(HttpStatus.OK.value(), code.getMsgDefault(), code.getMsgCode(), msgParams);
        }

        return oper;
    }

    public static <O> Operation<O> fail(String... message) {
        return fail(null, message);
    }

    public static <O> Operation<O> fail(Integer code, String... message) {
        return new Operation<>(code, false, null, message);
    }

    public static <O> Operation<O> failI18N(Integer code, String defaultMsg, String msgCode, Object... msgParams) {
        Operation<O> oper = fail();
        oper.translate(code, defaultMsg, msgCode, msgParams);
        return oper;
    }

    public static <O> Operation<O> failI18N(Code errCode, Object... msgParams) {
        Operation<O> oper = fail();
        if (errCode != null) {
            oper.translate(ERROR_CODE, errCode.getMsgDefault(), errCode.getMsgCode(), msgParams);
        }

        return oper;
    }

    public static <O> Operation<O> resultI18N(ProcessResult<O> res, Object... msgParams) {
        return res.isSuccess() ? succI18N(res.getResult(), res.getCode(), msgParams) : failI18N(res.getCode(), msgParams);
    }
}
