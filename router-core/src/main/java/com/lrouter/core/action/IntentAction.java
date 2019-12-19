package com.lrouter.core.action;

import android.content.Context;
import android.content.Intent;

import com.lrouter.core.RouterCenter;
import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.RouterRequest;

/**
 * 根据请求信息包装intent
 * @Author:lqc
 */
public class IntentAction implements RouterAction<Intent>{
    @Override
    public Intent invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
       return RouterCenter.getIntent(context,request,infoBean);
    }
}
