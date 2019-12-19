package com.lrouter.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author lqc
 * @version 1.0.0
 * @desc 插件入口
 * @date 2019/7/19 22:31
 */
class LRouterRootPlugin implements Plugin<Project> {

    Project root
    boolean isAssemble
    boolean isUpload
    boolean isUploadAll

    @Override
    void apply(Project project) {
        //project必须是root project
        if (project != project.rootProject) {
            throw new RuntimeException("'apply plugin: \'router.plugin\'' must be in root build.gradle")
        }
        root = project.rootProject
        project.extensions
        parseTasks(project)
        //是否使用本地model
        boolean enable
        try {
            enable = parseLocal()
        } catch (Exception e) {
            LRouterLoggerUtil.e(e.toString())
            enable = true
        }
        if (!enable) {
            LRouterLoggerUtil.e( "plugin 'router' is disabled, you can add 'router.enable=true' on root gradle.properties to enable router")
            return
        }
        def uploadAll = root.tasks.create(name:'routerUploadAll', group: 'LRouter')
        def uploadForClean = root.tasks.create(name:'routerUploadForClean', group: 'LRouter')
        uploadAll.dependsOn uploadForClean
        root.subprojects {
            it.beforeEvaluate {

            }
            it.afterEvaluate {

                // 对每个project都应用此UploadPlugin.
                it.plugins.apply(LRouterUploadPlugin)

                //是否使用本地model
                if(isAssemble&&it.project.plugins.hasPlugin(AppPlugin)){
                    it.plugins.apply(LRouterDynamicDependencePlugin)
                }
            }
        }
    }

    private boolean parseLocal() {
        def path =  root.properties.get("localRepo")
        String localPath
        if(path){
            localPath=path
        }else {
            localPath= new File(root.projectDir.absolutePath,"repo").absolutePath
        }
        String excludeModules =  root.properties.get("excludeModules", "") as String
        excludeModules = excludeModules.replaceAll(' ','')
        root.ext {
            excludes = excludeModules.length() == 0 ? [] : excludeModules.split(',')
            localMaven = localPath
        }
        //设置本地仓库路径
        root.subprojects {
            repositories {
                maven { url localPath}
            }
        }
        root.buildscript {
            repositories{
                maven { url localPath}
            }
        }

        return Boolean.parseBoolean(root.properties.get("router.enable",true))
    }

    /**
     * 判断是是否含有upload或者assemble任务
     */
    private void parseTasks(Project project){
        project.gradle.startParameter.taskNames.each {
            if (it.contains("assemble")||it.contains("Debug")||it.contains("Release")) {
                isAssemble = true
            } else if (it.contains('upload')) {
                isUpload = true
            }
            if (it == 'uploadAll') {
                isUploadAll = true
            }
        }
    }

}
