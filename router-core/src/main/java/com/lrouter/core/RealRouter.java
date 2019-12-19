package com.lrouter.core;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.SparseArray;

import com.lrouter.core.interceptor.RouterInterceptor;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * router具体处理器
 * @Author: lqc
 */
class RealRouter implements IRouter {

    private RouterRequest mRouterRequest;
    private RealRouter(String url){
        mRouterRequest = new RouterRequest();
        mRouterRequest.setPath(url);
    }
    private RealRouter(){

    }


    static IRouter create(String url) {
        return new RealRouter(url);
    }

    @Override
    public IRouter setCallback(RouterCallback callback) {
        mRouterRequest.setRouteCallback(callback);
        return this;
    }

    @Override
    public IRouter setRequestCode(int requestCode) {
        mRouterRequest.setRequestCode(requestCode);
        return this;
    }

    @Override
    public IRouter putObject(String key, Object value) {
        if (value == null) {
            return this;
        }
        Bundle bundle = mRouterRequest.getBundle();
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (byte) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (short) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (int) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (long) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (char) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof IBinder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bundle.putBinder(key, (IBinder) value);
            } else {
                LogUtil.e("putBinder() requires api 18.");
            }
        } else if (value instanceof ArrayList) {
            if (!((ArrayList) value).isEmpty()) {
                Object obj = ((ArrayList) value).get(0);
                if (obj instanceof Integer) {
                    bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
                } else if (obj instanceof String) {
                    bundle.putStringArrayList(key, (ArrayList<String>) value);
                } else if (obj instanceof CharSequence) {
                    bundle.putCharSequenceArrayList(key, (ArrayList<CharSequence>) value);
                } else if (obj instanceof Parcelable) {
                    bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                }
            }
        } else if (value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray<? extends Parcelable>) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else {
            LogUtil.i("Unknown object type: " + value.getClass().getName());
        }
        mRouterRequest.setBundle(bundle);
        return this;
    }

    @Override
    public IRouter addFlags(int flags) {
        mRouterRequest.setFlags(flags);
        return this;
    }

    @Override
    public IRouter setData(Uri data) {
        mRouterRequest.setData(data);
        return this;
    }

    @Override
    public IRouter setType(String type) {
        mRouterRequest.setType(type);
        return this;
    }

    @Override
    public IRouter setDataAndType(Uri data, String type) {
        mRouterRequest.setType(type);
        mRouterRequest.setData(data);
        return this;
    }

    @Override
    public IRouter setAction(String action) {
        mRouterRequest.setAction(action);
        return this;
    }

    @Override
    public IRouter setPath(String path) {
        mRouterRequest.setPath(path);
        return this;
    }

    @Override
    public IRouter anim(int enterAnim, int exitAnim) {
        mRouterRequest.setExitAnim(exitAnim);
        mRouterRequest.setEnterAnim(exitAnim);
        return this;
    }

    @Override
    public Intent getIntent(Context context) {
        Intent intent = RouterCenter.getIntent(context, mRouterRequest);
        return intent;
    }

    @Override
    public void startActivity(Context context, RouterCallback callback) {
        if(callback!=null){
            String path = mRouterRequest.getPath();
            callback.afterOpen(context,path);
            try {
                mRouterRequest.setActionType(RouterCenter.CLASS_ACTION);
                Object o =  RouterCenter.handleRequest(context, mRouterRequest);
                if(o!=null&&(Boolean) o){
                    callback.beforeOpen(context,path);
                }else {
                    callback.notFound(context,path);
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.error(context,path,e);
            }
        }else {
            try {
                RouterCenter.handleRequest(context, mRouterRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void startActivity(Context context) {
        startActivity(context,null);
    }




    @Override
    public Class getPathClass() {
        mRouterRequest.setActionType(RouterCenter.CLASS_ACTION);
        Class o = (Class) RouterCenter.handleRequest(null, mRouterRequest);
        return o;
    }

    @Override
    public Bundle getPathToBundle() {
        return UriCompact.parseExtras(Uri.parse(mRouterRequest.getPath()));
    }


    @Override
    public Fragment getFragment(Context context) {
        mRouterRequest.setActionType(RouterCenter.SUPPORT_FRAGMENT_ACTION);
        Fragment o = (Fragment) RouterCenter.handleRequest(context, mRouterRequest);
        return o;
    }

    @Override
    public android.support.v4.app.Fragment getSupportFragment(Context context) {
        mRouterRequest.setActionType(RouterCenter.SUPPORT_FRAGMENT_ACTION);
        android.support.v4.app.Fragment o = (android.support.v4.app.Fragment) RouterCenter.handleRequest(context, mRouterRequest);
        return o;
    }

    @Override
    public boolean invokerMethod() {
        return invokerMethod(null);
    }

    @Override
    public boolean invokerMethod(Context context) {
        mRouterRequest.setActionType(RouterCenter.METHOD_ACTION);
        Object o = RouterCenter.handleRequest(context, mRouterRequest);
        if(o!=null){
            return (Boolean) o;
        }
        return false;
    }

    @Override
    public IRouter addInterceptor(RouterInterceptor interceptor) {
        mRouterRequest.addInterceptor(interceptor);
        return this;
    }
}
