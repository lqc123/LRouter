package com.lrouter.core;

import android.net.Uri;
import android.os.Bundle;

import com.lrouter.core.interceptor.RouterInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * router传参包装类
 * @Author: lqc
 */
public class RouterRequest {
    /**
     * 默认参数
     */
    public static final int DEF_NUM=-1;
    /**
     * 跳转活动
     */
    private String action;
    /**
     * 协议
     */
    private String scheme;
    /**
     * 跳转回调，被拦截器后不会调用
     */
    private RouterCallback routeCallback;
    /**
     * 跳转请求码
     */
    private int requestCode = DEF_NUM;
    /**
     * 进入动画
     */
    private int enterAnim = DEF_NUM;
    /**
     * 跳转离开动画
     */
    private int exitAnim = DEF_NUM;
    /**
     * 携带数据
     */
    private Bundle bundle;
    /**
     * intent跳转 flag标记
     */
    private int flags;
    /**
     * 携带uri数据
     */
    private Uri data;
    /**
     * intent 中类型
     */
    private String type;
    /**
     * 跳转路径
     */
    private String path;
    /**
     * 拦截器
     */
    private ArrayList<RouterInterceptor> mInterceptors;
    /**
     * 处理类型
     */
    private int actionType;

    public int getFlags() {
        return flags;
    }

    public Uri getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    //    private int type;
    public RouterRequest(){

    }


    public String getAction() {
        return action;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RouterCallback getRouteCallback() {
        return routeCallback;
    }

    public void setRouteCallback(RouterCallback routeCallback) {
        this.routeCallback = routeCallback;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void addInterceptor(RouterInterceptor interceptor){
        if(mInterceptors==null){
            mInterceptors = new ArrayList<>();
        }
       mInterceptors.add(interceptor);
    }

    public List<RouterInterceptor> getInterceptors(){
        return mInterceptors;
    }

    public @RouterCenter.Action int getActionType() {
        return actionType;
    }

    public void setActionType(@RouterCenter.Action int actionType) {
        this.actionType = actionType;
    }
}
