package com.lrouter.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lrouter.annotation.Router;


@Router(value = "test2")
public class Test2Activity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        TextView tv = findViewById(R.id.tv_test2);
        Intent intent = getIntent();
        tv.setText(intent.getStringExtra("a")+intent.getStringExtra("b")+intent.getStringExtra("c"));
    }
}
