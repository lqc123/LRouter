package com.lrouter.plugin

/**
 * @author lqc
 * @version 1.0.0
 * @desc 注册常用设置
 * @date 2019/7/19 22:31
 */
class LRouterConstValue {
    static final String PLUGIN_NAME = "lRouter"
    /**
     * 生成 类名
     */
    static final String GENERATE_TO_CLASS_NAME = 'com/lrouter/core/RouterCenter'
    /**
     * 生成类 文件名
     */
    static final String GENERATE_TO_CLASS_FILE_NAME = GENERATE_TO_CLASS_NAME + '.class'
    /**
     * 生成类方法名
     */
    static final String GENERATE_TO_METHOD_NAME = 'register'
    /**
     * 动态注册标记
     */
    static final String GENERATE_REGISTER_FLAG = 'sAutoRegister'
    /**
     * 指定扫描路由接口名
     */
    static final String ROUTER_INTERFACE_NAME = 'com/lrouter/annotation/IRouterMapper'


    static final String SCAN_PACKAGE_NAME = 'com/lrouter/generate'
    /**
     * 指定扫描拦截器接口名
     */
    static final String INTERCEPTOR_INTERFACE_NAME = 'com/lrouter/annotation/IIntercepter'


    /**
     *注册方法名
     */
    static final String REGISTER_METHOD_NAME = 'map'
    /**
     * 本地仓库默认groupid
     */
    static final String DEF_GROUP_ID='com.local.maven'
    /**
     * 本地仓库默认版本号
     */
    static final String DEF_VERSION='local'
    /**
     * 本地仓库默认module id
     */
    static final String DEF_ARTIFACT_ID='default'


}