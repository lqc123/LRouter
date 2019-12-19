package com.lrouter.core;

/**
 *
 * 路由注册信息类
 * @Author: lqc
 */
public class RouterRegisterInfoBean {
    /**
     *跳转协议. 如: "http"
     */
    public String scheme;
    /**
     * 跳转路径
     */
    public String path;
    /**
     * 注册class信息
     */
    public Class clazz;
    /**
     * 注册方法
     */
    public Func methodInvoker;

    public RouterRegisterInfoBean(String scheme, String path, Class clazz, Func methodInvoker) {
        this.scheme = scheme;
        this.path = path;
        this.clazz = clazz;
        this.methodInvoker = methodInvoker;
    }
}
