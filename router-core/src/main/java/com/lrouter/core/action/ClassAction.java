package com.lrouter.core.action;

import android.content.Context;

import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.RouterRequest;

/**
 * 返回请求信息中class
 * @Author:lqc
 */
public class ClassAction implements RouterAction<Class>{
    @Override
    public Class invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
        return infoBean.clazz;
    }
}
