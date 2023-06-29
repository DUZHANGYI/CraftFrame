package com.craft.frame.boot.web;

import com.craft.frame.boot.web.config.InterceptorConfig;
import com.craft.frame.boot.web.config.RedisConfig;
import com.craft.frame.boot.web.security.config.SecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author DURR
 * @desc 配置类入口
 * @date 2023/6/23 23:13
 */

@Configuration
@ComponentScan(basePackages = {"com.craft.frame.boot.web"})
@Import({SecurityConfig.class, RedisConfig.class, InterceptorConfig.class})
public class WebAutoConfiguration {

}
