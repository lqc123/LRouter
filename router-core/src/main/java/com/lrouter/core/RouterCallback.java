package com.lrouter.core;

import android.content.Context;
import android.net.Uri;

/**
 * 跳转页面回调接口
 *
 * @Author: lqc
 */
public interface RouterCallback {
    /**
     * 未查询到注册信息
     *
     * @param context 当前上下文
     * @param uri     跳转路径
     */
    void notFound(Context context, String uri);

    /**
     * 处理注册信息之前
     *
     * @param context 当前上下文
     * @param uri     跳转路径
     */
    void beforeOpen(Context context, String uri);

    /**
     * 处理注册信息之后
     *
     * @param context 当前上下文
     * @param uri     跳转路径
     */
    void afterOpen(Context context, String uri);

    /**
     * 处理注册信息发生异常时调用
     *
     * @param context 当前上下文
     * @param uri     跳转路径
     * @param e       异常信息
     */

    void error(Context context, String uri, Throwable e);
}
