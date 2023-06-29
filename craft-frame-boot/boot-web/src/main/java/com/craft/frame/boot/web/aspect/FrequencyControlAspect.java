package com.craft.frame.boot.web.aspect;

import com.craft.frame.boot.web.annotation.FrequencyControl;
import com.craft.frame.boot.web.cache.RedisCache;
import com.craft.frame.boot.web.constants.CacheKeyConstants;
import com.craft.frame.boot.web.exception.BusinessException;
import com.craft.frame.boot.web.exception.CommonErrorEnum;
import com.craft.frame.boot.web.utils.RequestHolder;
import com.craft.frame.boot.web.utils.SpringElUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author DURR
 * @desc 频控实现切面
 * @date 2023/6/25 20:07
 */
@Aspect
@Component
public class FrequencyControlAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyControlAspect.class);

    private final RedisCache redisUtils;

    public FrequencyControlAspect(RedisCache redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Around("@annotation(com.craft.frame.boot.web.annotation.FrequencyControl)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FrequencyControl[] annotationsByType = method.getAnnotationsByType(FrequencyControl.class);
        Map<String, FrequencyControl> keyMap = new HashMap<>();
        for (int i = 0; i < annotationsByType.length; i++) {
            FrequencyControl frequencyControl = annotationsByType[i];
            //默认方法限定名+注解排名（可能多个）
            String prefix = StringUtils.isBlank(frequencyControl.prefixKey()) ? SpringElUtils.getMethodKey(method) + ":index:" + i : frequencyControl.prefixKey();
            String key = StringUtils.EMPTY;
            switch (frequencyControl.target()) {
                case EL:
                    key = SpringElUtils.parseSpEl(method, joinPoint.getArgs(), frequencyControl.spEl());
                    break;
                case IP:
                    key = RequestHolder.get().getIp();
                    break;
                case UID:
                    key = RequestHolder.get().getUid();
            }
            keyMap.put(CacheKeyConstants.RATE_LIMIT_KEY + prefix + ":" + key, frequencyControl);
        }
        //批量获取redis统计的值
        List<String> keyList = new ArrayList<>(keyMap.keySet());
        List<Integer> countList = redisUtils.mget(keyList, Integer.class);
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            Integer count = countList.get(i);
            FrequencyControl frequencyControl = keyMap.get(key);
            if (Objects.nonNull(count) && count >= frequencyControl.count()) {//频率超过了
                LOGGER.warn("frequencyControl limit key:{},count:{}", key, count);
                throw new BusinessException(CommonErrorEnum.FREQUENCY_LIMIT);
            }
        }
        try {
            return joinPoint.proceed();
        } finally {
            //不管成功还是失败，都增加次数
            keyMap.forEach((k, v) -> redisUtils.inc(k, v.time(), v.unit()));
        }
    }

}
