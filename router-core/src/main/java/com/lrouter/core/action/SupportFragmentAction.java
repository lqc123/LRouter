package com.lrouter.core.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.RouterRequest;
import com.lrouter.core.UriCompact;

/**
 * supportfragment实例
 * @Author:lqc
 */
public class SupportFragmentAction implements RouterAction<Fragment> {
    @Override
    public Fragment invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
        Bundle bundle = request.getBundle();
        Bundle extras = UriCompact.parseExtras(Uri.parse(request.getPath()));
        if(bundle==null){
            bundle = extras;
        }else {
            bundle.putAll(extras);
        }
        return Fragment.instantiate(context,infoBean.clazz.getName(),bundle);
    }
}
