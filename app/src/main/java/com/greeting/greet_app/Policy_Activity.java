package com.greeting.greet_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Policy_Activity extends AppCompatActivity {

    Activity activity;
    TextView tv_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        activity=this;
        tv_text=findViewById(R.id.tv_text);
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}