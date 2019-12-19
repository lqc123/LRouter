package com.lrouter.core.action;

import android.content.Context;

import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.RouterRequest;

/**
 * 处理类接口
 * @param <T> 处理返回类型
 */
public interface RouterAction<T> {
    /**
     * 执行
     * @param context 当前上下文
     * @param request 请求实体
     * @param infoBean 请求信息
     * @return 泛型指定类型
     */
    T invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean);
}
