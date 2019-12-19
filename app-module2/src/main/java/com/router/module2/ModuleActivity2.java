package com.router.module2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lrouter.annotation.Router;

@Router("module2")
public class ModuleActivity2 extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("module2");
        setContentView(textView);
    }
}
