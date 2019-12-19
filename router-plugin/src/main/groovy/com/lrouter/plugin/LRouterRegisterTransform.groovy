package com.lrouter.plugin;

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * @author lqc* @version 1.0.0* @desc 扫描class和jar，并把注册路由添加到routerinitz中
 * @date 2019/7/19 22:31
 */
class LRouterRegisterTransform extends Transform {

    Project project
    static ArrayList<String> registerList = new ArrayList<>()
    static File sInitClassFile

    LRouterRegisterTransform(Project project) {
        this.project = project
    }

    /**
     * 任务名称
     * @return
     */
    @Override
    String getName() {
        return LRouterConstValue.PLUGIN_NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 扫描范围
     * @return
     */
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(Context context, Collection<TransformInput> inputs
                   , Collection<TransformInput> referencedInputs
                   , TransformOutputProvider outputProvider
                   , boolean isIncremental) throws IOException, TransformException, InterruptedException {

        LRouterLoggerUtil.i('Start scan register info in jar file.')
        registerList.clear()
        long startTime = System.currentTimeMillis()
        boolean leftSlash = File.separator == '/'

        inputs.each { TransformInput input ->

            // scan all jars
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.name
                // rename jar files
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4)
                }
                // input file
                File src = jarInput.file

                // output file
                File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //scan jar file to find classes
                if (LRouterScanUtil.shouldProcessPreDexJar(src.absolutePath)) {
                    LRouterScanUtil.scanJar(src, dest)
                }
                FileUtils.copyFile(src, dest)
            }
            // scan class files
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                String root = directoryInput.file.absolutePath
                if (!root.endsWith(File.separator))
                    root += File.separator
                //遍历目录下的每个文件
                directoryInput.file.eachFileRecurse { File file ->
                    def path = file.absolutePath.replace(root, '')
                    if (file.isFile()) {
                        def entryName = path
                        if (!leftSlash) {
                            entryName = entryName.replaceAll("\\\\", "/")
                        }
                        LRouterScanUtil.checkInitClass(entryName, new File(dest.absolutePath + File.separator + path))
                        if (LRouterScanUtil.shouldProcessClass(entryName)) {
                            LRouterScanUtil.scanClass(file)
                        }
                    }
                }
                // copy to dest
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }

        LRouterLoggerUtil.i('Scan finish, current cost time ' + (System.currentTimeMillis() - startTime) + "ms")

        if (sInitClassFile) {
            LRouterAsmInject processor = new LRouterAsmInject()
            File file = LRouterRegisterTransform.sInitClassFile
            File dest = outputProvider.getContentLocation(
                    "LRouter", TransformManager.CONTENT_CLASS,
                    ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY)
            processor.insertInitCodeTo(file, dest.getAbsolutePath())
        }
        LRouterLoggerUtil.i("Generate code finish, current cost time: " + (System.currentTimeMillis() - startTime) + "ms")

    }
}
