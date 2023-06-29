package com.craft.frame.boot.web.security.config;

import com.craft.frame.boot.web.security.annotation.Anonymous;
import com.craft.frame.boot.web.exception.BusinessException;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author DURR
 * @desc 添加匿名访问的接口
 * @date 2023/6/26 16:21
 */
@Configuration
public class AnonymousConfig implements InitializingBean, ApplicationContextAware {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");

    private ApplicationContext applicationContext;

    private final List<String> urls = new ArrayList<>();

    private static final String ASTERISK = "*";

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);
            //查看类上或者方法上是否同时有 @Anonymous 和 @PreAuthorize 注解 如果有则抛异常
            PreAuthorize methodPreAuthorize = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
            PreAuthorize classPreAuthorize = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);
            // 获取类上边的注解, 替代path variable 为 *
            Anonymous classAnonymous = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
            // 获取方法上边的注解 替代path variable 为 *
            Anonymous methodAnonymous = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
            if ((methodPreAuthorize != null && methodAnonymous != null)
                    || (classAnonymous != null && methodPreAuthorize != null)
                    || (classAnonymous != null && classPreAuthorize != null)
                    || (classPreAuthorize != null && methodAnonymous != null)) {
                Set<PathPattern> patterns = info.getPathPatternsCondition() == null ? null :
                        info.getPathPatternsCondition().getPatterns();
                String msg = String.format("Anonymous注解和PreAuthorize注解不可同时使用,错误路径:%s", patterns);
                throw new BusinessException(msg);
            }
            addUrl(classAnonymous, info);
            addUrl(methodAnonymous, info);
        });
    }

    private void addUrl(Anonymous anonymous, RequestMappingInfo info) {
        Optional.ofNullable(anonymous)
                .flatMap(a -> Optional.ofNullable(info.getPathPatternsCondition()))
                .ifPresent(condition -> condition.getPatterns()
                        .forEach(pattern ->
                                urls.add(RegExUtils.replaceAll(pattern.getPatternString(), PATTERN, ASTERISK))));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<String> getUrls() {
        return urls.stream().distinct().collect(Collectors.toList());
    }

}
