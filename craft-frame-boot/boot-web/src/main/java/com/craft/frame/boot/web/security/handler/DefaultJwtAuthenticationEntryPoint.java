package com.craft.frame.boot.web.security.handler;


import com.alibaba.fastjson2.JSON;
import com.craft.frame.boot.web.response.Operation;
import com.craft.frame.boot.web.utils.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DURR
 * @desc 用来解决匿名用户访问需要权限才能访问的资源时的异常
 * @date 2023/6/24 14:51
 */
public class DefaultJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	/**
	 * 当用户尝试访问需要权限才能的REST资源而不提供Token或者Token错误或者过期时，
	 * 将调用此方法返回错误信息
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
		ServletUtils.renderString(response,JSON.toJSONString(Operation.fail(HttpStatus.UNAUTHORIZED.value(), msg)));
	}
}