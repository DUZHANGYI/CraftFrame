package com.craft.frame.boot.web.intecepter;

import com.craft.frame.boot.web.model.RequestInfo;
import com.craft.frame.boot.web.security.filter.JwtAuthorizationFilter;
import com.craft.frame.boot.web.utils.RequestHolder;
import com.craft.frame.boot.web.utils.ServletUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author DURR
 * @desc 默认信息收集的拦截器
 * @date 2023/6/25 20:31
 */

@Order(1)
public class DefaultCollectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestInfo info = new RequestInfo();
        info.setUid(Optional.ofNullable(request.getAttribute(JwtAuthorizationFilter.ATTRIBUTE_UID)).map(Object::toString).orElse(null));
        info.setIp(ServletUtils.getClientIP(request));
        RequestHolder.set(info);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }

}
