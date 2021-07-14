package com.chendroid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : zhaochen
 * @date : 2021/7/12
 * @description : PreInflater 注解
 */
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface PreInflater {
    //参数 布局 id
    int layout();
    // 布局被提前初始化的线程指定， 默认为 io
    String scheduler() default "io";
}
