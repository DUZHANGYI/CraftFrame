package com.craft.frame.boot.web.response;

import com.craft.frame.commons.lang.res.Code;
import com.craft.frame.commons.lang.res.ProcessResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

/**
 * @author DURR
 * @desc 响应单个数据
 * @date 2023/6/23 22:07
 */
@ApiModel(description = "响应数据(单个)")
public class SingleData<D> extends Response {
    @ApiModelProperty("数据内容")
    private D data;

    public SingleData() {
        super(ResponseType.DATA);
    }

    public static <Data> SingleData<Data> succ(Data data, String... message) {
        SingleData<Data> sd = new SingleData<>();
        sd.setCode(HttpStatus.OK.value());
        sd.setSuccess(true);
        sd.setData(data);
        sd.setMessage(message);
        return sd;
    }

    public static <Data> SingleData<Data> succI18N(Data data, String defaultMsg, String msgCode, Object... msgParams) {
        SingleData<Data> sd = succ(data);
        sd.translate(SUCCESS_CODE, defaultMsg, msgCode, msgParams);
        return sd;
    }

    public static <Data> SingleData<Data> succI18N(Data data, Code code, Object... msgParams) {
        SingleData<Data> sd = succ(data);
        if (code != null) {
            sd.translate(SUCCESS_CODE, code.getMsgCode(), code.getMsgCode(), msgParams);
        }

        return sd;
    }

    public static <Data> SingleData<Data> fail(String... message) {
        SingleData<Data> sd = new SingleData<>();
        sd.setCode(HttpStatus.BAD_REQUEST.value());
        sd.setSuccess(false);
        sd.setMessage(message);
        return sd;
    }

    public static <Data> SingleData<Data> failI18N(Integer code, String defaultMsg, String msgCode, Object... msgParams) {
        SingleData<Data> sd = fail();
        sd.translate(code, defaultMsg, msgCode, msgParams);
        return sd;
    }

    public static <Data> SingleData<Data> failI18N(Integer code, Code errCode, Object... msgParams) {
        SingleData<Data> sd = fail();
        if (errCode != null) {
            sd.translate(code, errCode.getMsgDefault(), errCode.getMsgCode(), msgParams);
        }
        return sd;
    }

    public static <Data> SingleData<Data> resultI18N(ProcessResult<Data> res, Object... msgParams) {
        return res.isSuccess() ? succI18N(res.getResult(), res.getCode(), msgParams) : failI18N(ERROR_CODE, res.getCode(), msgParams);
    }

    public D getData() {
        return this.data;
    }

    public void setData(D data) {
        this.data = data;
    }
}

