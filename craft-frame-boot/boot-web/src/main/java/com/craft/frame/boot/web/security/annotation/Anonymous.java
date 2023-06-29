package com.craft.frame.boot.web.security.annotation;

import java.lang.annotation.*;

/**
 * @author DURR
 * @desc 匿名访问不鉴权注解
 * @date 2023/6/26 16:22
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous {
}
