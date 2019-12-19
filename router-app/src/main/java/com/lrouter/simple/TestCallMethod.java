package com.lrouter.simple;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.lrouter.annotation.Router;
import com.lrouter.core.Func;

public class TestCallMethod implements Func{
    @Router("callMethod")
    public  void call(Context context, Bundle bundle){
        Toast.makeText(context,bundle.getString("test"),Toast.LENGTH_LONG).show();
    }
}
