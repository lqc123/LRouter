package com.lrouter.core.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.Func;
import com.lrouter.core.RouterRequest;
import com.lrouter.core.UriCompact;

/**
 * 根据请求信息调用对应的方法
 * @Author:lqc
 */
public class MethodAction implements RouterAction<Boolean>{
    @Override
    public Boolean invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
        Uri uri = Uri.parse(request.getPath());
        Bundle bundle = request.getBundle();
        if (bundle != null) {
            bundle.putAll(UriCompact.parseExtras(uri));
        } else {
            Bundle b = UriCompact.parseExtras(uri);
            bundle=b;
        }
        try {
            Func func = (Func) infoBean.clazz.newInstance();
            func.call(context,bundle);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
