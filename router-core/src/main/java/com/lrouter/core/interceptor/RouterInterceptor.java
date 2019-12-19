package com.lrouter.core.interceptor;

import com.lrouter.core.RouterRequest;

/**
 * router调用拦截器
 * @Author:lqc
 */
public interface RouterInterceptor {
    /**
     * 拦截请求信息方法
     * @param request 请求信息
     * @return true拦截，false不拦截
     */
    boolean intercept(RouterRequest request);
}
