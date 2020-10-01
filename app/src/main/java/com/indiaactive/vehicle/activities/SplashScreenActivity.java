package com.indiaactive.vehicle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.indiaactive.vehicle.R;

import static android.content.ContentValues.TAG;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("login",false);
        getLocationPermission();
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
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            }else{
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");

        switch(requestCode){
            case 1:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    //initialize our map
                }
            }
        }
    }
}