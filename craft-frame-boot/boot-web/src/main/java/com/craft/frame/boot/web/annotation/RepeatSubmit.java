package com.craft.frame.boot.web.annotation;

import java.lang.annotation.*;

/**
 * @author DURR
 * @desc 自定义注解防止表单重复提交
 * @date 2023/6/25 21:00
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 提示消息
     */
    String message() default "不允许重复提交，请稍候再试";
}