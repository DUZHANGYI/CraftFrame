package com.craft.frame.boot.web.security.filter;

import com.craft.frame.boot.web.security.JwtUtils;
import com.craft.frame.boot.web.security.SecurityTool;
import com.craft.frame.boot.web.security.config.properties.JwtProperties;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DURR
 * @desc 类描述
 * @date 2023/6/24 14:32
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityTool securityTool;

    private final JwtUtils jwtUtils;

    private final JwtProperties properties;

    public static final String ATTRIBUTE_UID = "uid";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityTool securityTool,
                                  JwtUtils jwtUtils, JwtProperties properties) {
        super(authenticationManager);
        this.securityTool = securityTool;
        this.jwtUtils = jwtUtils;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(properties.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (token == null || !token.startsWith(properties.getPrefix())) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }
        String tokenValue = token.replace(properties.getPrefix(), StringUtils.EMPTY);
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            String previousToken = securityTool.getToken(jwtUtils.getId(tokenValue));
            if (!token.equals(previousToken)) {
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
                return;
            }
            authentication = securityTool.getAuthentication(tokenValue);
        } catch (JwtException e) {
            logger.error("Invalid jwt : " + e.getMessage());
        }
        String uid = jwtUtils.getId(tokenValue);
        request.setAttribute(ATTRIBUTE_UID, uid);
        securityTool.setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
