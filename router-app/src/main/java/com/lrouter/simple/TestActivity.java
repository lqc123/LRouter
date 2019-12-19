package com.lrouter.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lrouter.annotation.Router;


@Router(value = "test")
public class TestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        String a = intent.getStringExtra("a");
        String b = intent.getStringExtra("b");
        TextView tv = findViewById(R.id.tv_content);
        tv.setText(a+b);
    }
}
