package com.craft.frame.boot.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DURR
 * @desc 添加了基础的拦截器, 可由子类继承重写
 * @date 2023/6/26 10:14
 */

@Configuration
public abstract class FrameWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HandlerInterceptor collectorInterceptor;

    @Autowired
    private HandlerInterceptor repeatSubmitInterceptor;

    protected List<String> collectorWhite = new ArrayList<>();

    protected List<String> repeatSubmitWhite = new ArrayList<>();

    /**
     * 初始化白名单
     */
    public abstract void initWhiteList();

    /**
     * 添加其他拦截器
     *
     * @param interceptor 注册器
     */
    public abstract void addElseInterceptors(InterceptorRegistry interceptor);

    @Override
    public void addInterceptors(InterceptorRegistry interceptor) {
        this.initWhiteList();
        //信息手机拦截器
        interceptor.addInterceptor(collectorInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(collectorWhite);

        //防止重复提交拦截器
        interceptor.addInterceptor(repeatSubmitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(repeatSubmitWhite);

        //添加子类的拦截器
        this.addElseInterceptors(interceptor);
    }

}
