package com.lrouter.core;

/**
 * router门面类，单例
 *  @Author: lqc
 */
public class LRouter {
    /**
     * 默认schmem
     */
    private static final String DEF_SCHEME = "Router://";
    private volatile static LRouter sLRouter;
    private static String sScheme = DEF_SCHEME;
    private boolean isInit=false;
    private static boolean sDebug=false;

    /**
     * 获取实例
     * @return {@link LRouter}
     */
    public static LRouter getInstance() {
        sLRouter = new LRouter();
        if (sLRouter == null) {
            synchronized (LRouter.class) {
                if (sLRouter == null) {
                    sLRouter = new LRouter();
                }
            }
        }
        return sLRouter;
    }

    /**
     * 是否是debug模式
     * @return
     */
    public static boolean isDebug() {
        return sDebug;
    }

    /**
     * 获取协议
     * @return 协议
     */
    public String getScheme() {
        return sScheme;
    }

    /**
     * 开启日志
     */
    public void openLog() {
        LogUtil.setEnabled(true);
    }

    /**
     * 关闭日主
     */
    public void closeLog() {
        LogUtil.setEnabled(false);
    }

    /**
     * 创建RealRouter
     * @param path 跳转路径
     * @return {@link IRouter}
     */
    public IRouter build(String path) {
        return RealRouter.create(path);
    }

    /**
     * 设置跳转协议
     * @param scheme 协议
     */
    public void setScheme(String scheme) {
        sScheme = scheme;
    }

    /**
     * 初始化
     * @param isDebug true 调试模式  false生产模式
     */
    public void init(boolean isDebug) {
        sDebug=isDebug;
        if(!isInit){
            RouterCenter.register();
            RouterCenter.initAction();
            isInit=true;
        }
    }
}
