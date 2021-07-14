package com.chendroid.processor;

import com.chendroid.annotation.PreInflater;
import com.google.auto.service.AutoService;
import com.hendraanggrian.RParser;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * @author : zhaochen
 * @date : 2021/7/12
 * @description : 预加载布局的处理者
 */
@AutoService(Processor.class)
public class PreInflaterProcess extends AbstractProcessor {


    private static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = new HashSet<>(Collections.<Class<? extends Annotation>>singletonList(PreInflater.class));

    private RParser parser ;

    /**
     * 注解对应生成的目标类的类名
     */
    private String targetProcessorClassName = "";


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationSet = new HashSet<>();
        annotationSet.add(PreInflater.class.getCanonicalName());

        return annotationSet;
    }

    // 主要方法，在里面写对使用了 @PreInflater 的类进行处理
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取 R 文件 layout 布局文件
        parser = RParser.builder(processingEnv)
                .setSupportedAnnotations(SUPPORTED_ANNOTATIONS)
                .setSupportedTypes("layout")
                .build();

        parser.scan(roundEnv);

        Map<Integer, Info> map = new HashMap<>();
        // 获取到所有使用了 PreInflater 注释了的元素，即类
        for (Element element : roundEnv.getElementsAnnotatedWith(PreInflater.class)) {
            if (element instanceof  TypeElement) {
                String schedule = element.getAnnotation(PreInflater.class).scheduler();
                int layoutResId = element.getAnnotation(PreInflater.class).layout();

                String holderClassName = ((TypeElement) element).getQualifiedName().toString();
                targetProcessorClassName = buildTargetProcessorClassName();

                String layoutResStr = null;
                String packageName = null;

                for (String path : holderClassName.split("\\.")) {
                    // 拼接包名
                    if (packageName == null) {
                        packageName = path;
                    } else {
                        packageName = packageName + "." + path;
                    }

                    layoutResStr = parser.parse(packageName, layoutResId);
                    //这个作用是什么？
                    if (!layoutResStr.equals(String.valueOf(layoutResId))) {
                        break;
                    }
                }
                if (layoutResStr == null || layoutResStr.equals(String.valueOf(layoutResId))) {
                    throw new IllegalStateException("process " + holderClassName + " failed!");
                }
                map.put(layoutResId, new Info(layoutResStr, schedule));
            }

            if (map.size() > 0) {
                // 存在注解的布局
                generatePreInflaterMapper(map);
            }
        }

        return true;
    }

    /**
     * 注解生成真正类的地方;
     * 使用字符串拼接实现
     * @param map: 使用了注解生成的 map
     */
    private void generatePreInflaterMapper(Map<Integer, Info> map) {
        // todo
        StringBuilder stringBuilder = new StringBuilder();
        String packageName = "com.chendroid.learning.preinflater";
        stringBuilder.append("package ").append(packageName).append(";\n\n");
        stringBuilder.append("import java.util.HashMap;\n");
        stringBuilder.append("import java.util.Map;\n");
        stringBuilder.append("import com.chendroid.preinflater.PreInflaterManager;\n");

        stringBuilder.append("public final class ").append(targetProcessorClassName).append(" {\n\n");
        stringBuilder.append("      public static void inject() {\n");
        for (int key : map.keySet()) {
            stringBuilder.append("      reInflaterManager.addPreInflateInfo(new PreInflaterManager.PreInflateInfo(")
                    .append(map.get(key).layoutResStr)
                    .append(", \"")
                    .append(map.get(key).scheduler)
                    .append("\"));\n");
        }

        stringBuilder.append("  }\n");
        stringBuilder.append("}\n");

        System.out.println("string builder is " + stringBuilder);
        JavaFileObject object = null;

        try {
            object = processingEnv.getFiler().createSourceFile(packageName + "." + targetProcessorClassName);

            Writer writer = object.openWriter();
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String buildTargetProcessorClassName() {
        String moduleName = processingEnv.getOptions().get("moduleNameOfPreInflater");

        String finalHeaderName = moduleName.substring(0, 1).toUpperCase() + moduleName.substring(1);
        return finalHeaderName + "$R2InflaterMapper";
    }

    public static class Info {
        public String layoutResStr;
        public String scheduler;

        public Info(String layoutResStr, String scheduler) {
            this.layoutResStr = layoutResStr;
            this.scheduler = scheduler;
        }
    }

}
