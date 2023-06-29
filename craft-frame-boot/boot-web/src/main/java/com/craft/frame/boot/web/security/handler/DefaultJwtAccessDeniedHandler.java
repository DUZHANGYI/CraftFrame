package com.craft.frame.boot.web.security.handler;

import com.alibaba.fastjson2.JSON;
import com.craft.frame.boot.web.response.Operation;
import com.craft.frame.boot.web.utils.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DURR
 * @desc 用来解决认证过的用户访问无权限资源时的异常
 * @date 2023/6/24 14:47
 */
public class DefaultJwtAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * 当用户尝试访问需要权限才能的REST资源而权限不足的时候，
	 * 将调用此方法发送403响应以及错误信息
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		String msg = String.format("请求访问：%s，权限不足", request.getRequestURI());
		ServletUtils.renderString(response, JSON.toJSONString(Operation.fail(HttpStatus.FORBIDDEN.value(), msg)));
	}

}
