package com.indiaactive.vehicle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.SMSAdapter;
import com.indiaactive.vehicle.datamodels.RootMessage;
import com.indiaactive.vehicle.interfaces.SMSPlaceholder;


import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberActivity extends AppCompatActivity {

    String user,password,senderid,channel,num,text;
    int DCS,flash,route;
    String resRandom;
    TextInputEditText number;
    Button go;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_mobile_number);


        init();
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    void init(){
        Random random = new Random();
        progressBar = findViewById(R.id.progressbar);
        number = findViewById(R.id.phoneNumberEt);
        go = findViewById(R.id.go);
        user = "mdrentals";
        password = "india123";
        senderid = "HONMAP";
        channel = "Trans";
        DCS = 0;
        flash = 0;
        route = 0;
        resRandom = generatePIN();
        //String otp = String.format("%04d", random.nextInt(10000));
        text = "Your Hire on Map OTP is "+resRandom;
    }

    private void sendSMS() {
        String mobile = number.getText().toString().trim();
        if(mobile.isEmpty()){
            number.setError("Cannot be empty");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(number);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        SMSPlaceholder smsPlaceholder = SMSAdapter.createAPI();
        Call<RootMessage> call = smsPlaceholder.sendOTP(user,password,senderid,channel,DCS,flash,mobile,text,route);
        call.enqueue(new Callback<RootMessage>() {
            @Override
            public void onResponse(Call<RootMessage> call, Response<RootMessage> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    RootMessage rm = response.body();
                    try{
                        if(rm.getErrorCode().equals("000")){
                            Toast.makeText(MobileNumberActivity.this, "SMS SENT", Toast.LENGTH_SHORT).show();
                            //start new Activity
                            Intent intent = new Intent(MobileNumberActivity.this,OTPActivity.class);
                            intent.putExtra("OTP",resRandom);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                        }
                    }catch (Exception e){
                        Toast.makeText(MobileNumberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MobileNumberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RootMessage> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MobileNumberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String generatePIN() {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        Log.i("Random",String.valueOf(randomPIN));
        return String.valueOf(randomPIN);
    }
}