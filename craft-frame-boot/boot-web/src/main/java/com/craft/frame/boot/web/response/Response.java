package com.craft.frame.boot.web.response;

import com.alibaba.fastjson2.JSONObject;
import com.craft.frame.boot.web.utils.ServletUtils;
import com.craft.frame.commons.lang.res.Code;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DURR
 * @desc web响应对象
 * @date 2023/6/23 21:56
 */

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "响应对象")
public class Response {
    protected static final String SUCCESS_MSG = Code.CODE_SUCC.getMsgDefault();
    protected static final String ERROR_MSG = Code.CODE_ERROR.getMsgDefault();
    protected static final int SUCCESS_CODE = HttpStatus.OK.value();
    protected static final int ERROR_CODE = HttpStatus.BAD_REQUEST.value();

    @ApiModelProperty(value = "服务器处理结果", allowableValues = "true,false")
    private boolean success;
    @ApiModelProperty("服务器返回的状态码")
    private Integer code;
    @ApiModelProperty("服务器返回的信息")
    private String message;
    @ApiModelProperty("如果服务器的支持国际化,此字段代表国际化编码")
    private String msgCode;
    @ApiModelProperty("服务返回的结果类型")
    private ResponseType type;

    protected Response(ResponseType type) {
        this.type = type;
    }

    protected void translate(Integer code, String defaultMsg, String msgCode, Object... params) {
        if (!StringUtils.isBlank(msgCode)) {
            HttpServletRequest request = ServletUtils.getRequest();
            RequestContext requestContext = new RequestContext(request);
            this.setCode(code);
            this.setMsgCode(msgCode);
            this.setMessage(requestContext.getMessage(msgCode, params, defaultMsg));
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected void setMessage(String... message) {
        if (message != null && message.length > 0) {
            this.setMessage(StringUtils.join(message, ','));
        } else {
            this.setMessage(this.isSuccess() ? SUCCESS_MSG : ERROR_MSG);
        }
    }

    public ResponseType getType() {
        return this.type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        if (code != null) {
            this.code = code;
        } else {
            this.code = this.isSuccess() ? SUCCESS_CODE : ERROR_CODE;
        }
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public enum ResponseType {
        //操作类型
        OPER,

        //单个数据
        DATA,

        //集合
        LIST,

        //分页
        PAGE,

        //发生错误
        ERROR;

        ResponseType() {
        }
    }

}
