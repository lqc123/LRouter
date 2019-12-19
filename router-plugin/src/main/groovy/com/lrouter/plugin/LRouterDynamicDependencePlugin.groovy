package com.lrouter.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author lqc
 * @version 1.0.0
 * @desc 动态依赖plugin
 * @date 2019/7/19 22:31
 */
class LRouterDynamicDependencePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        String localMavenModules = project.rootProject.properties.get(project.path, "") as String
        if (localMavenModules) {
            String[] localModules=localMavenModules.length() == 0 ? []: localMavenModules.split(',')
            for (String name : localModules) {
                name = name.replaceAll(':', '-')
                LRouterLoggerUtil.i( "compile project with name ${name} is local")
                project.dependencies.add("compile", "${LRouterConstValue.DEF_GROUP_ID}:${name}:+", {
                    exclude group: LRouterConstValue.DEF_GROUP_ID, module: LRouterConstValue.DEF_ARTIFACT_ID
                })
            }
        }
        project.android.registerTransform(new LRouterRegisterTransform(project))
    }
}
