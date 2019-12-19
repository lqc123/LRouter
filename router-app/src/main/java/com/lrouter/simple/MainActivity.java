package com.lrouter.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lrouter.core.LRouter;
import com.lrouter.core.RouterCallback;
import com.lrouter.core.RouterRequest;
import com.lrouter.core.interceptor.RouterInterceptor;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LRouter.getInstance().init(true);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LRouter.getInstance().build("module?a=true&b=1").startActivity(v.getContext());
            }
        });
        findViewById(R.id.tv_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LRouter.getInstance().build("test2?c=c").putObject("a", "a").putObject("b", "b").addInterceptor(new RouterInterceptor() {
                    @Override
                    public boolean intercept(RouterRequest request) {
                        Toast.makeText(v.getContext(),"intercept",Toast.LENGTH_LONG).show();

                        return false;
                    }
                }).setCallback(new RouterCallback() {
                    @Override
                    public void notFound(Context context, String uri) {

                    }

                    @Override
                    public void beforeOpen(Context context, String uri) {

                    }

                    @Override
                    public void afterOpen(Context context, String uri) {

                    }

                    @Override
                    public void error(Context context, String uri, Throwable e) {

                    }
                }).startActivity(v.getContext().getApplicationContext());
            }
        });


        findViewById(R.id.callMethod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LRouter.getInstance().build("callMethod?test=hhh").invokerMethod(v.getContext());
            }
        });

    }
}
