package com.chendroid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhaochen@ZhiHu Inc.
 * @intro
 * @since 2020/6/22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface RouterTest {

    //@Target({ElementType.TYPE}), 表明了注解的范围 TYPE：类、接口、枚举、注解类型
    //@Retention(RetentionPolicy.CLASS) 编译时注解： CLASS

    /**
     * 具体的 url 值, 可为多个， 路由匹配的值
     */
    String[] value();

}