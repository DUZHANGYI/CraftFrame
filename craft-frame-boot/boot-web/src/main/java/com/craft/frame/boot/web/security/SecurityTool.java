package com.craft.frame.boot.web.security;

import com.craft.frame.boot.web.cache.RedisCache;
import com.craft.frame.boot.web.constants.CacheKeyConstants;
import com.craft.frame.boot.web.utils.ServletUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DURR
 * @desc 描述
 * @date 2023/6/26 16:10
 */

@Component
public class SecurityTool {

    private final RedisCache redisCache;

    private final JwtUtils jwtUtils;

    public SecurityTool(RedisCache redisCache, JwtUtils jwtUtils) {
        this.redisCache = redisCache;
        this.jwtUtils = jwtUtils;
    }

    public void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = jwtUtils.getClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        String userName = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userName, token, authorities);
    }

    public String generateToken(String username, String id, List<String> roles, boolean isRememberMe) {
        String token = jwtUtils.generateToken(username, id, roles, isRememberMe);
        redisCache.set(CacheKeyConstants.USER_TOKEN_KEY + id, token);
        return token;
    }

    public String getCurrUserToken() {
        HttpServletRequest request = ServletUtils.getRequest();
        return jwtUtils.getUserToken(request);
    }

    public void lostCurrUserToken() {
        String currUserToken = getCurrUserToken();
        loseToken(currUserToken);
    }

    public String getToken(String uid) {
        return redisCache.getStr(CacheKeyConstants.USER_TOKEN_KEY + uid);
    }

    /**
     * 使Token失效
     */
    public void loseToken(String token) {
        String uid = jwtUtils.getId(token);
        loseTokenByUid(uid);
    }

    public void loseTokenByUid(String uid) {
        redisCache.del(CacheKeyConstants.USER_TOKEN_KEY + uid);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        String roleClaims = jwtUtils.getRoleClaims(claims);
        return Arrays.stream(roleClaims.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
