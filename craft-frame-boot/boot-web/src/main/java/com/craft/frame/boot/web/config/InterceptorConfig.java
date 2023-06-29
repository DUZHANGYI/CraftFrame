package com.craft.frame.boot.web.config;

import com.craft.frame.boot.web.intecepter.DefaultCollectorInterceptor;
import com.craft.frame.boot.web.intecepter.DefaultRepeatSubmitInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author DURR
 * @desc 描述
 * @date 2023/6/26 14:53
 */
@Configuration
public class InterceptorConfig {

    @Bean("collectorInterceptor")
    @ConditionalOnMissingBean(DefaultCollectorInterceptor.class)
    public HandlerInterceptor collectorInterceptor() {
        return new DefaultCollectorInterceptor();
    }

    @Bean("repeatSubmitInterceptor")
    @ConditionalOnMissingBean(DefaultRepeatSubmitInterceptor.class)
    public HandlerInterceptor repeatSubmitInterceptor() {
        return new DefaultRepeatSubmitInterceptor();
    }

}
