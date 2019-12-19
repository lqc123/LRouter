package com.lrouter.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.MavenPlugin

/**
 * @author lqc
 * @version 1.0.0
 * @desc 打包上传插件
 * @date 2019/7/19 22:31
 */
class LRouterUploadPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        def root = project.rootProject
        def uploadAll = root.tasks.getByName("routerUploadAll")
        def uploadForClean = root.tasks.getByName("routerUploadForClean")
        String[] excludes = root.excludes
        uploadForClean.dependsOn "${project.path}:clean"

        if (project == root
                || project.plugins.hasPlugin('com.android.application')) {
            return
        }
        project.plugins.apply(MavenPlugin)
        project.uploadArchives {
            repositories {
                mavenDeployer {
                    def version = project.properties.get("version")
                    pom.groupId = LRouterConstValue.DEF_GROUP_ID
                    pom.version = version== "unspecified"?LRouterConstValue.DEF_VERSION:version
                    pom.artifactId =project.path.replaceAll(':','-')
                    repository(url: project.uri(project.rootProject.localMaven))
                    pom.whenConfigured { pom ->
                        pom.dependencies.forEach { dep ->
                            if (dep.getVersion() == "unspecified") {
                                dep.setGroupId(LRouterConstValue.DEF_GROUP_ID)
                                dep.setVersion(LRouterConstValue.DEF_VERSION)
                                dep.artifactId=LRouterConstValue.DEF_ARTIFACT_ID
                            }
                        }
                    }
                }
            }
        }

        // 过滤忽略model
        if (excludes.contains(project.path)) {
            return
        }

        String name = project.path.replaceAll(":", "")
        def upload = project.rootProject.tasks.create(name: "routerUpload$name", group: 'LRouter', dependsOn: "${project.path}:uploadArchives")
        upload.doLast {
            LRouterLoggerUtil.i("upload ${project.path} to local maven successful!")
        }

        uploadAll.dependsOn upload
    }


}
