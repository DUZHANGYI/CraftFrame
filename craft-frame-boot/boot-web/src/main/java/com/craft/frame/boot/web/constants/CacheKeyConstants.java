package com.craft.frame.boot.web.constants;

/**
 * @author DURR
 * @desc 缓存的key
 * @date 2023/6/26 18:15
 */
public class CacheKeyConstants {

    /**
     * 用户Token redis key
     */
    public static final String USER_TOKEN_KEY = "user_token:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

}
