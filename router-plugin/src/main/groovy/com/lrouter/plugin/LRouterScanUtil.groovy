package com.lrouter.plugin

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile


/**
 * @author lqc
 * @version 1.0.0
 * @desc 扫描工具类
 * @date 2019/7/19 22:31
 */
class LRouterScanUtil {

    /**
     * 扫描jar
     * @param jarFile 源文件
     * @param destFile 输出文件
     */
    static void scanJar(File jarFile, File destFile) {
        if (jarFile) {
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()// 判断是否目生成类
                if (LRouterConstValue.GENERATE_TO_CLASS_FILE_NAME == entryName) {
                    LRouterLoggerUtil.i("find init code to class -->> "+destFile.name)
                    LRouterRegisterTransform.sInitClassFile = destFile
                }else if (entryName.startsWith(LRouterConstValue.SCAN_PACKAGE_NAME)) {
                    InputStream inputStream = file.getInputStream(jarEntry)
                    ClassReader cr = new ClassReader(inputStream)
                    ClassWriter cw = new ClassWriter(cr, 0)
                    ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw)
                    cr.accept(cv, ClassReader.EXPAND_FRAMES)
                    inputStream.close()
                }
            }
            file.close()
        }
    }

    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    static boolean shouldProcessClass(String entryName) {
        return entryName != null && entryName.startsWith(LRouterConstValue.SCAN_PACKAGE_NAME)
    }

   static void checkInitClass(String entryName, File file) {
        if (entryName == null || !entryName.endsWith(".class"))
            return
//        entryName = entryName.substring(0, entryName.lastIndexOf('.'))
        if (LRouterConstValue.GENERATE_TO_CLASS_FILE_NAME == entryName)
            LRouterRegisterTransform.sInitClassFile = file
    }
    /**
     * 扫描class
     * @param class 文件
     */
    static void scanClass(File file) {
        def inputStream = new FileInputStream(file)
        scanClass(inputStream)
    }

    static void scanClass(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, 0)
        inputStream.close()
    }

    static class ScanClassVisitor extends ClassVisitor {
        ScanClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }
        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
            if(interfaces!=null&&interfaces.length!=0){
                LRouterLoggerUtil.i(interfaces[0])
                if(interfaces.contains(LRouterConstValue.ROUTER_INTERFACE_NAME)||name.contains(LRouterConstValue.INTERCEPTOR_INTERFACE_NAME)){
                    def list = LRouterRegisterTransform.registerList
                    if(!list.contains(name)){
                        list.add(name)
                        LRouterLoggerUtil.i("register code to class-->> "+name)
                    }
                }
            }

        }
    }

}