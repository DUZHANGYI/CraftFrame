package com.craft.frame.boot.web.response;

import com.craft.frame.commons.lang.res.Code;
import com.craft.frame.commons.lang.res.PageResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author DURR
 * @desc 响应分页
 * @date 2023/6/23 22:27
 */
@ApiModel(description = "响应分页")
public class PageData<T> extends ListData<T> {
	@ApiModelProperty("分页大小")
	private Long pageSize;
	@ApiModelProperty("分页页号")
	private Long pageNum;
	@ApiModelProperty("总记录笔数")
	private Long total;

	public PageData() {
		this.setType(ResponseType.PAGE);
	}

	public static <P> PageData<P> succ(List<P> rows, Integer pageSize, Integer pageNum, Long total, String... message) {
		Long pageSizeL = pageSize == null ? null : (long) pageSize;
		Long pageNumL = pageNum == null ? null : (long) pageNum;
		return succ(rows, pageSizeL, pageNumL, total, message);
	}

	public static <P> PageData<P> succI18N(List<P> rows, Integer pageSize, Integer pageNum, Long total, String defaultMsg, String msgCode, Object... msgParams) {
		PageData<P> pageData = succ(rows, pageSize, pageNum, total);
		pageData.translate(SUCCESS_CODE, defaultMsg, msgCode, msgParams);
		return pageData;
	}

	public static <P> PageData<P> succI18N(List<P> rows, Integer pageSize, Integer pageNum, Long total, Code code, Object... msgParams) {
		PageData<P> pageData = succ(rows, pageSize, pageNum, total);
		if (code != null) {
			pageData.translate(SUCCESS_CODE, code.getMsgDefault(), code.getMsgCode(), msgParams);
		}

		return pageData;
	}

	public static <P> PageData<P> succ(List<P> rows, Long pageSize, Long pageNum, Long total, String... message) {
		PageData<P> pageData = new PageData<>();
		pageData.setSuccess(true);
		pageData.setCode(SUCCESS_CODE);
		pageData.setRows(rows);
		pageData.setPageSize(pageSize);
		pageData.setPageNum(pageNum);
		pageData.setTotal(total);
		pageData.setMessage(message);
		return pageData;
	}

	public static <P> PageData<P> succI18N(List<P> rows, Long pageSize, Long pageNum, Long total, String defaultMsg, String msgCode, Object... msgParams) {
		PageData<P> pageData = succ(rows, pageSize, pageNum, total);
		pageData.translate(SUCCESS_CODE, defaultMsg, msgCode, msgParams);
		return pageData;
	}

	public static <P> PageData<P> succI18N(List<P> rows, Long pageSize, Long pageNum, Long total, Code code, Object... msgParams) {
		PageData<P> pageData = succ(rows, pageSize, pageNum, total);
		if (code != null) {
			pageData.translate(SUCCESS_CODE, code.getMsgDefault(), code.getMsgCode(), msgParams);
		}

		return pageData;
	}

	public static <P> PageData<P> fail(String... message) {
		PageData<P> pageData = new PageData<>();
		pageData.setCode(HttpStatus.BAD_REQUEST.value());
		pageData.setSuccess(false);
		pageData.setMessage(message);
		return pageData;
	}

	public static <P> PageData<P> failI18N(String defaultMsg, String msgCode, Object... msgParams) {
		PageData<P> pageData = fail();
		pageData.translate(ERROR_CODE, defaultMsg, msgCode, msgParams);
		return pageData;
	}

	public static <P> PageData<P> failI18N(Integer code, Code errCode, Object... msgParams) {
		PageData<P> pageData = fail();
		if (errCode != null) {
			pageData.translate(code, errCode.getMsgDefault(), errCode.getMsgCode(), msgParams);
		}

		return pageData;
	}

	public static <P> PageData<P> resultI18N(PageResult<P> page, Object... msgParams) {
		return page.isSuccess()
				? succI18N(page.getResult(), page.getSize(), page.getCurrent(), page.getTotal(), page.getCode(), msgParams)
				: failI18N(ERROR_CODE, page.getCode(), msgParams);
	}

	public Long getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageNum() {
		return this.pageNum;
	}

	public void setPageNum(Long pageNum) {
		this.pageNum = pageNum;
	}

	public Long getTotal() {
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
