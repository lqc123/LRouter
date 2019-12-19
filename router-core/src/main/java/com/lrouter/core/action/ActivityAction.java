package com.lrouter.core.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lrouter.core.RouterCenter;
import com.lrouter.core.RouterRegisterInfoBean;
import com.lrouter.core.RouterRequest;

/**
 * activity跳转处理
 * @Author:lqc
 */
public class ActivityAction implements RouterAction<Boolean>{

    @Override
    public Boolean invoke(Context context, RouterRequest request, RouterRegisterInfoBean infoBean) {
        Intent intent = RouterCenter.getIntent(context, request,infoBean);
        if (intent != null) {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            int requestCode = request.getRequestCode();
            if (requestCode >= 0) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                } else {
                    throw new RuntimeException("can not startActivityForResult context " + context);
                }
            } else {
                context.startActivity(intent);
            }
            int exitAnim = request.getExitAnim();
            int enterAnim = request.getEnterAnim();

            if (context instanceof Activity&&enterAnim!=RouterRequest.DEF_NUM&&exitAnim!=RouterRequest.DEF_NUM) {
                ((Activity) context).overridePendingTransition(enterAnim,exitAnim);
            }
            return true;
        }
        return false;
    }
}
