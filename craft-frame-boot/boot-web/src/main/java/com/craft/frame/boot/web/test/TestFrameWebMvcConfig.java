package com.craft.frame.boot.web.test;

import com.craft.frame.boot.web.config.FrameWebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @author DURR
 * @desc 描述
 * @date 2023/6/26 15:04
 */

@Configuration
public class TestFrameWebMvcConfig extends FrameWebMvcConfig {

    @Override
    public void initWhiteList() {
        collectorWhite.add("11");
        repeatSubmitWhite.add("22");
    }

    @Override
    public void addElseInterceptors(InterceptorRegistry interceptor) {
        System.err.println("测试子类重写FrameWebMvcConfig");
    }


}
