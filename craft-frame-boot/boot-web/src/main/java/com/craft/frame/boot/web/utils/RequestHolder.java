package com.craft.frame.boot.web.utils;

import com.craft.frame.boot.web.model.RequestInfo;

/**
 * @author DURR
 * @desc 请求上下文
 * @date 2023/6/25 20:21
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> THREADLOCAL = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        THREADLOCAL.set(requestInfo);
    }

    public static RequestInfo get() {
        return THREADLOCAL.get();
    }

    public static void remove() {
        THREADLOCAL.remove();
    }

}
