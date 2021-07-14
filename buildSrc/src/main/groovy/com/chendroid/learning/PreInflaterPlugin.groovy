package com.chendroid.learning
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author : zhaochen
 * @date : 2021/7/12
 * @description : 插件
 */
class PreInflaterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.android.registerTransform(new PreInflaterTransform(project))
    }
}