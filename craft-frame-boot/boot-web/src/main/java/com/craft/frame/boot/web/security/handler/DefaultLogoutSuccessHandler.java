package com.craft.frame.boot.web.security.handler;

import com.alibaba.fastjson2.JSON;
import com.craft.frame.boot.web.response.Operation;
import com.craft.frame.boot.web.security.SecurityTool;
import com.craft.frame.boot.web.utils.ServletUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DURR
 * @desc 退出成功的接口
 * @date 2023/6/24 15:09
 */
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private SecurityTool securityTool;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        securityTool.lostCurrUserToken();
        String msg = "退出成功";
        ServletUtils.renderString(response, JSON.toJSONString(Operation.succ(HttpStatus.OK.value(), msg)));
    }

}
