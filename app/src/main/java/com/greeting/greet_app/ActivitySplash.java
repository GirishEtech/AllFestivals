package com.greeting.greet_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.greeting.greet_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivitySplash extends AppCompatActivity {
    ConnectionDetector cd;
    Activity activity;
    AppLangSessionManager appLangSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        this.activity = this;
        this.cd = new ConnectionDetector(getApplicationContext());
        final SharedPreferences mPrefs = getSharedPreferences("label", 0);
        SharedPreferences.Editor edit = mPrefs.edit();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ActivitySplash.this.startActivity(new Intent(ActivitySplash.this, MainActivity.class));
                ActivitySplash.this.finish();
            }
        }, 3000);
    }
}
