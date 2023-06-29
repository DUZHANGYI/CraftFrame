package com.craft.frame.boot.web.intecepter;

import com.alibaba.fastjson2.JSON;
import com.craft.frame.boot.web.annotation.RepeatSubmit;
import com.craft.frame.boot.web.response.Operation;
import com.craft.frame.boot.web.security.config.properties.JwtProperties;
import com.craft.frame.boot.web.cache.RedisCache;
import com.craft.frame.boot.web.constants.CacheKeyConstants;
import com.craft.frame.boot.web.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author DURR
 * @desc 防止重复提交拦截器
 * @date 2023/6/25 21:01
 */
public class DefaultRepeatSubmitInterceptor implements HandlerInterceptor {
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    @Resource
    private JwtProperties properties;

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    Operation<?> operation = Operation.fail(annotation.message());
                    ServletUtils.renderString(response, JSON.toJSONString(operation));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交
     */
    @SuppressWarnings("unchecked")
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        //本次参数和系统时间
        String nowParams = JSON.toJSONString(request.getParameterMap());
        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());
        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader(properties.getHeader()));
        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CacheKeyConstants.REPEAT_SUBMIT_KEY + url + submitKey;
        Object sessionObj = redisCache.get(cacheRepeatKey, Object.class);
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) &&
                        compareTime(nowDataMap, preDataMap, annotation.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        redisCache.set(cacheRepeatKey, cacheMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return StringUtils.equals(nowParams, preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }

}
