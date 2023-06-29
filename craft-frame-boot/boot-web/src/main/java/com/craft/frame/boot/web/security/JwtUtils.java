package com.craft.frame.boot.web.security;

import com.craft.frame.boot.web.security.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.List;

/**
 * @author DURR
 * @desc JWT工具类
 * @date 2023/6/23 22:32
 */
@Component
public class JwtUtils {

    private static final String ROLE_CLAIMS = "roles";
    private static final String JWT_TYPE = "JWT";

    private final JwtProperties jwtProp;

    private final SecretKey SECRET_KEY;

    public JwtUtils(JwtProperties jwtProp) {
        this.jwtProp = jwtProp;
        // 生成足够的安全随机密钥，以适合符合规范的签名
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProp.getSecret());
        SECRET_KEY = Keys.hmacShaKeyFor(apiKeySecretBytes);
    }

    public String generateToken(String username, String id, List<String> roles, boolean isRememberMe) {
        long expiration = isRememberMe ? jwtProp.getExpirationRemember() : jwtProp.getExpiration();
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        String tokenPrefix = Jwts.builder()
                .setHeaderParam("type", JWT_TYPE)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .claim(ROLE_CLAIMS, String.join(",", roles))
                .setId(id)
                .setIssuer("SnailClimb")
                .setIssuedAt(createdDate)
                .setSubject(username)
                .setExpiration(expirationDate)
                .compact();
        // 添加 token 前缀;
        return jwtProp.getPrefix() + " " + tokenPrefix;
    }

    public String getId(String token) {
        Claims claims = getClaims(token);
        return claims.getId();
    }

    public String getUserToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProp.getHeader());
        return token.replace(jwtProp.getPrefix(), StringUtils.EMPTY);
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String getRoleClaims(Claims claims) {
        return (String) claims.get(ROLE_CLAIMS);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}
