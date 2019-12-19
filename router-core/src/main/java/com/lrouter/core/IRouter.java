package com.lrouter.core;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnimRes;

import com.lrouter.core.interceptor.RouterInterceptor;

/**
 * @Author: lqc
 */
public interface IRouter {


    /**
     * 设置回调
     */
    IRouter setCallback(RouterCallback callback);

    /**
     * 调用 <code>startActivityForResult</code>.
     */
    IRouter setRequestCode(int requestCode);

    /**
     * bundle.putXXX(String key, XXX value).
     */
    IRouter putObject(String key, Object value);


    /**
     * 添加flag
     *
     * @see Intent#addFlags(int)
     */
    IRouter addFlags(int flags);

    /**
     * @see Intent#setData(Uri)
     */
    IRouter setData(Uri data);

    /**
     * @see Intent#setType(String)
     */
    IRouter setType(String type);

    /**
     * @see Intent#setDataAndType(Uri, String)
     */
    IRouter setDataAndType(Uri data, String type);

    /**
     * @see Intent#setAction(String)
     */
    IRouter setAction(String action);

    /**
     * 设置跳转路径
     *
     * @param path 跳转路径
     * @return {@link IRouter}
     */
    IRouter setPath(String path);

    /**
     * 设置跳转动画
     *
     * @param enterAnim 进入动画
     * @param exitAnim  离开动画
     * @return {@link IRouter}
     */
    IRouter anim(@AnimRes int enterAnim, @AnimRes int exitAnim);



    /**
     * 根据路径获取intent
     *
     * @return {@link IRouter}
     */
    Intent getIntent(Context context);


    /**
     * 启动activity
     *
     * @param context {@link Context}
     * @param callback 跳转回调{@link RouterCallback}
     */
    void startActivity(Context context, RouterCallback callback);

    /**
     * 启动activity
     *
     * @param context {@link Context}
     */
    void startActivity(Context context);

    /**
     * 根据路径或许当前class
     *
     * @return {@link Class}
     */
    Class getPathClass();

    /**
     * 根据路径获取bundle对象
     *
     * @return {@link Bundle}
     */
    Bundle getPathToBundle();

    /**
     * 根据路径获取fragment
     * @param context {@link Context}
     * @return {@link Fragment}
     */
    Fragment getFragment(Context context);

    /**
     * 根据路径获取surportfragment
     * @param context
     * @return {@link android.support.v4.app.Fragment}
     */
    android.support.v4.app.Fragment getSupportFragment(Context context);


    /**
     * 执行方法
     * @return true 成功  false失败
     */
    boolean invokerMethod();

    /**
     * 执行方法
     * @param context 当前上下文
     * @return {@link com.lrouter.core.action.RouterAction#invoke(Context, RouterRequest, RouterRegisterInfoBean)}
     */
    boolean invokerMethod(Context context);

    /**
     * 添加拦截器
     * @param interceptor {@link RouterInterceptor}
     */
    IRouter addInterceptor(RouterInterceptor interceptor);


}
