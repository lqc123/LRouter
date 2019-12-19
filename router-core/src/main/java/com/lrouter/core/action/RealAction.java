package com.lrouter.core.action;

import android.content.Context;

import com.lrouter.core.LogUtil;
import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.interceptor.RouterInterceptor;
import com.lrouter.core.RouterRequest;

import java.util.List;

/**
 * 拦截器处理类
 * @Author:lqc
 */
public class RealAction implements RouterAction{
    private List<RouterAction> mActionList;

    public RealAction(List<RouterAction> actionList){
        this.mActionList = actionList;
    }
    @Override
    public Object invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
        if(infoBean==null){
            LogUtil.e("register info is null");
            return null;
        }
        List<RouterInterceptor> interceptors = request.getInterceptors();
        if(interceptors!=null&&!interceptors.isEmpty()){
            for (RouterInterceptor interceptor:interceptors){
                if(interceptor.intercept(request)){
                    return null;
                }
            }
        }
        return mActionList.get(request.getActionType()).invoke(context,request,infoBean);
    }
}
