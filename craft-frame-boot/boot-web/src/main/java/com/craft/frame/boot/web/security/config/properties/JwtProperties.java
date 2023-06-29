package com.craft.frame.boot.web.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author DURR
 * @desc 类描述
 * @date 2023/6/23 22:40
 */

@ConfigurationProperties(prefix = "jwt.config")
public class JwtProperties {

	/**
	 * 过期时间默认1小时
	 */
	private long expiration = 60 * 60L;

	/**
	 * 记住我 过期时间默认7天
	 */
	private long expirationRemember = 60 * 60 * 24 * 7L;

	/**
	 * JWT签名密钥
	 */
	private String secret = "441E0738EC706461B33D9FC3CF86B2ADFA27FDD6DD90A7426660D1304804DD5C";

	/**
	 * JWT token 请求头
	 */
	private String header = "Authorization";

	/**
	 * token前缀
	 */
	private String prefix = "Bearer";

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public long getExpirationRemember() {
		return expirationRemember;
	}

	public void setExpirationRemember(long expirationRemember) {
		this.expirationRemember = expirationRemember;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
