package com.lrouter.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

/**
 * @author lqc
 * @version 1.0.0
 * @desc 利用javassist，扫描注册
 * @date 2019/7/19 22:31
 */
 class LRouterJavasisstInject implements LRouterInject{
     @Override
     void insertInitCodeTo(File initClassFile, String path) {
         ClassPool pool = ClassPool.getDefault()
         pool.appendClassPath(path)
         //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
         pool.appendClassPath(project.android.bootClasspath[0].toString())

         File dir = new File(path)
         if (dir.isDirectory()) {
             LRouterLoggerUtil.i("start traverse ...")
             dir.eachFileRecurse { File file ->
                 String filePath = file.absolutePath
                 def name = file.getName()
                 LRouterLoggerUtil.i("file path-->>"+filePath)
                 if (filePath.contains(LRouterConstValue.GENERATE_TO_CLASS_FILE_NAME)) {

                     CtClass ctClass = pool.getCtClass(LRouterConstValue.GENERATE_TO_CLASS_NAME.replaceAll("/",".")+"."+name.substring(0,name.lastIndexOf(".")))
                     if (ctClass.isFrozen()) ctClass.defrost()

                     CtMethod ctMapMethod = ctClass.getDeclaredMethod(LRouterConstValue.GENERATE_TO_METHOD_NAME)
                     LRouterLoggerUtil.i("insert method-->>" + ctMapMethod)


                     //在方法开头插入代码
                     LRouterRegisterTransform.registerList.each {
                         LRouterLoggerUtil.i("register class-->>"+it)
                         ctMapMethod.insertBefore(it.replaceAll("/",".") + ".map();")
                     }
                     ctClass.writeFile(path)
                     //释放
                     ctClass.detach()


                 }

             }
         }
         LRouterLoggerUtil.i("end traverse ...")
     }
 }