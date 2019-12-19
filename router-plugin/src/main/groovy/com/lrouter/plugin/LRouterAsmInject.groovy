package com.lrouter.plugin


import org.apache.commons.io.IOUtils
import org.objectweb.asm.*

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * @author lqc
 * @version 1.0.0
 * @desc asm注册生成类
 * @date 2019/7/19 22:31
 */
class LRouterAsmInject implements LRouterInject{


    /**
     * 处理class的注入
     * @param file class文件
     * @return 修改后的字节码文件内容
     */
    private byte[] insertInitCodeIntoClassFile(File file) {
        def optClass = new File(file.getParent(), file.name + ".opt")

        FileInputStream inputStream = new FileInputStream(file)
        FileOutputStream outputStream = new FileOutputStream(optClass)

        def bytes = referHackWhenInit(inputStream)
        outputStream.write(bytes)
        inputStream.close()
        outputStream.close()
        if (file.exists()) {
            file.delete()
        }
        optClass.renameTo(file)
        return bytes
    }

    /**
     * 扫描jar文件
     * @param 包含 GENERATE_TO_CLASS_FILE_NAME 类
     * @return
     */
    private File insertInitCodeIntoJarFile(File jarFile) {
        if (jarFile) {
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            def file = new JarFile(jarFile)
            Enumeration enumeration = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = file.getInputStream(jarEntry)
                jarOutputStream.putNextEntry(zipEntry)
                if (LRouterConstValue.GENERATE_TO_CLASS_FILE_NAME == entryName) {

                    LRouterLoggerUtil.i('find init code to class -->> ' + entryName)

                    def bytes = referHackWhenInit(inputStream)
                    jarOutputStream.write(bytes)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            file.close()

            if (jarFile.exists()) {
                jarFile.delete()
            }
            optJar.renameTo(jarFile)
        }
        return jarFile
    }

    /**
     * 创建访问者，来处理注册类
     * @param inputStream
     * @return
     */
    private byte[] referHackWhenInit(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ClassVisitor cv = new MyClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv,0)
        return cw.toByteArray()
    }

    @Override
    void insertInitCodeTo(File file, String path) {
        if (file.getName().endsWith('.jar')){
            insertInitCodeIntoJarFile(file)
        } else{
            insertInitCodeIntoClassFile(file)
        }

    }

    class MyClassVisitor extends ClassVisitor {

        MyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc,
                                  String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
            if (name == LRouterConstValue.GENERATE_TO_METHOD_NAME) {
                LRouterLoggerUtil.i("MethodVisitor-->>"+name)
                mv = new RouteMethodVisitor(Opcodes.ASM5, mv)
            }
            return mv
        }
    }

    class RouteMethodVisitor extends MethodVisitor {

         RouteMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv)
        }

        @Override
        void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                mv.visitInsn(Opcodes.ICONST_1)
                mv.visitFieldInsn(Opcodes.PUTSTATIC,LRouterConstValue.GENERATE_TO_CLASS_NAME,LRouterConstValue.GENERATE_REGISTER_FLAG,"Z")
                LRouterRegisterTransform.registerList.each { name ->
                    LRouterLoggerUtil.i("register-->> "+name)
//                    name = name.replaceAll("/", ".")
                    //创建一个无参构造方法组件实例
                    mv.visitTypeInsn(Opcodes.NEW, name)
                    mv.visitInsn(Opcodes.DUP)
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V", false)
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, name, LRouterConstValue.REGISTER_METHOD_NAME, "()V", false)
                }
            }
            super.visitInsn(opcode)
        }
        @Override
        void visitMaxs(int maxStack, int maxLocals) {
            LRouterLoggerUtil.i("maxStack="+maxStack+"   "+"maxLocals="+maxLocals)
            super.visitMaxs(maxStack + 4, maxLocals+1)
        }
    }
}