package com.craft.frame.boot.web.response;

import com.craft.frame.commons.lang.res.Code;
import com.craft.frame.commons.lang.res.ProcessResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DURR
 * @desc 响应数组
 * @date 2023/6/23 22:24
 */
@ApiModel(description = "响应数组")
public class ListData<T> extends Response {
	@ApiModelProperty("数组返回结果")
	private List<T> rows;

	public ListData() {
		super(ResponseType.LIST);
	}

	public static <L> ListData<L> succ(List<L> rows, String... message) {
		ListData<L> data = new ListData<>();
		data.setCode(HttpStatus.OK.value());
		data.setSuccess(true);
		data.setRows(rows);
		data.setMessage(message);
		return data;
	}

	public static <L> ListData<L> succI18N(List<L> rows, String defaultMsg, String msgCode, Object... msgParams) {
		ListData<L> data = succ(rows);
		data.translate(SUCCESS_CODE, defaultMsg, msgCode, msgParams);
		return data;
	}

	public static <L> ListData<L> succI18N(List<L> rows, Code code, Object... msgParams) {
		ListData<L> data = succ(rows);
		if (code != null) {
			data.translate(SUCCESS_CODE, code.getMsgDefault(), code.getMsgCode(), msgParams);
		}

		return data;
	}

	public static <L> ListData<L> fail(String... message) {
		ListData<L> data = new ListData<>();
		data.setCode(HttpStatus.BAD_REQUEST.value());
		data.setSuccess(false);
		data.setRows(new ArrayList<>());
		data.setMessage(message);
		return data;
	}

	public static <L> ListData<L> failI18N(String defaultMsg, String msgCode, Object... msgParams) {
		ListData<L> data = fail();
		data.translate(ERROR_CODE, defaultMsg, msgCode, msgParams);
		return data;
	}

	public static <L> ListData<L> failI18N(Integer code, Code errCode, Object... msgParams) {
		ListData<L> data = fail();
		if (errCode != null) {
			data.translate(code, errCode.getMsgDefault(), errCode.getMsgCode(), msgParams);
		}

		return data;
	}

	public static <L> ListData<L> resultI18N(ProcessResult<List<L>> result, Object... msgParams) {
		return result.isSuccess() ? succI18N(result.getResult(), result.getCode(), msgParams) : failI18N(ERROR_CODE, result.getCode(), msgParams);
	}

	public List<T> getRows() {
		return this.rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}