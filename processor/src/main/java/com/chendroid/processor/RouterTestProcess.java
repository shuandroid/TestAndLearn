package com.chendroid.processor;

import com.chendroid.annotation.RouterTest;
import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * @author zhaochen@ZhiHu Inc.
 * @intro
 * @since 2020/6/22
 */

//@AutoService(Processor.class)
class RouterTestProcess extends AbstractProcessor {

    /**
     * 初始化处理器，一般在这里获取我们需要的工具类
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    /**
     * 指定注解处理器是注册给哪个注解的
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationSet = new HashSet<>();
        annotationSet.add(RouterTest.class.getCanonicalName());
        return annotationSet;
    }

    /**
     * 指定java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {



        return false;
    }
}
