package com.indiaactive.vehicle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.indiaactive.vehicle.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("login",false);
        new Handler(Looper.myLooper(), message -> {
            Toast.makeText(SplashScreenActivity.this,message.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }
            }
        },2500);
    }
}