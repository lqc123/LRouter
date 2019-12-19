package com.router.module;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lrouter.annotation.Router;

@Router("module")
public class ModuleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("this is ModuleActivity");
        setContentView(textView);
    }
}
