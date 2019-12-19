package com.lrouter.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;

import com.lrouter.core.action.ActivityAction;
import com.lrouter.core.action.ClassAction;
import com.lrouter.core.action.FragmentAction;
import com.lrouter.core.action.IntentAction;
import com.lrouter.core.action.MethodAction;
import com.lrouter.core.action.RealAction;
import com.lrouter.core.action.RouterAction;
import com.lrouter.core.action.SupportFragmentAction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由处理中心
 * @Author: lqc
 */
public class RouterCenter {
    private static Map<String, RouterRegisterInfoBean> sMappings = new HashMap<>();
    private static Map<String, RouterRegisterInfoBean> sInjectors = new HashMap<>();
    private static List sActions=new ArrayList<RouterAction>();
    static final int ACTIVITY_ACTION=0;
    static final int CLASS_ACTION =1;
    static final int FRAGMENT_ACTION =2;
    static final int INTENT_ACTION =3;
    static final int METHOD_ACTION =4;
    static final int SUPPORT_FRAGMENT_ACTION =5;

    /**
     * 处理类型
     */
    @IntDef({ACTIVITY_ACTION, CLASS_ACTION, FRAGMENT_ACTION, INTENT_ACTION, METHOD_ACTION, SUPPORT_FRAGMENT_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    @interface Action{

    }
    static boolean sAutoRegister=false;

    public static void register() {
    }

    public static void map(String path, Class clazz) {
        if (path == null) {
            throw new NullPointerException("path can not be null");
        }
        Uri parse = Uri.parse(path);
        sMappings.put(parse.getPath(), new RouterRegisterInfoBean(parse.getScheme(), parse.getPath(), clazz, null));
    }

    public static void mapMethod(String path, Class clazz) {
        if (path == null) {
            throw new NullPointerException("path can not be null");
        }
        Uri parse = Uri.parse(path);
        sInjectors.put(parse.getPath(), new RouterRegisterInfoBean(parse.getScheme(), parse.getPath(), clazz, null));
    }
    public static void initAction(){
        sActions.add(new ActivityAction());
        sActions.add(new ClassAction());
        sActions.add(new FragmentAction());
        sActions.add(new IntentAction());
        sActions.add(new MethodAction());
        sActions.add(new SupportFragmentAction());
    }


    /**
     * 根据url获取intent
     *
     * @param context
     * @param request
     * @return
     */
     static Intent getIntent(Context context, RouterRequest request) {
        Uri uri = Uri.parse(request.getPath());
        String path = uri.getPath();
        RouterRegisterInfoBean infoBean = sMappings.get(path);
         if (infoBean == null) {
             return null;
         }
        return getIntent(context,request,infoBean);
    }
    public static Intent getIntent(Context context, RouterRequest request,RouterRegisterInfoBean infoBean) {
        Uri uri = Uri.parse(request.getPath());
        Intent intent = new Intent(context, infoBean.clazz);
        Uri data = request.getData();
        if (data != null) {
            intent.setData(data);
        }
        String type = request.getType();
        if (type != null) {
            intent.setType(type);
        }
        String action = request.getAction();
        if (action != null) {
            intent.setAction(action);
        }
        Bundle bundle = request.getBundle();
        if (bundle != null) {
            bundle.putAll(UriCompact.parseExtras(uri));
        } else {
            Bundle b = UriCompact.parseExtras(uri);
            bundle=b;
        }
        intent.putExtras(bundle);
        return intent;
    }


    static Object handleRequest(Context context, RouterRequest request) {
        Uri uri = Uri.parse(request.getPath());
        String path = uri.getPath();
        RouterRegisterInfoBean infoBean = request.getActionType()== METHOD_ACTION?sInjectors.get(path):sMappings.get(path);
       return new RealAction(sActions).invoke(context,request,infoBean);
    }

}
