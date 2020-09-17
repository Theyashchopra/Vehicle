package com.indiaactive.vehicle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.datamodels.UserData;
import com.indiaactive.vehicle.interfaces.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwoStepVerificationActivity extends AppCompatActivity {

    Button register;
    TextView name,email;
    TextInputEditText number;
    ImageView imageView;
    LottieAnimationView progress;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String personName,personGivenName,personEmail,personId;
    Uri personPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_step_verification);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(TwoStepVerificationActivity.this);
        initialise(acct);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle(acct);
            }
        });
    }

    private void initialise(GoogleSignInAccount acct){
        name = findViewById(R.id.name);
        email = findViewById(R.id.mail);
        progress = findViewById(R.id.google_loading);
        imageView = findViewById(R.id.profile_google);
        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        register = findViewById(R.id.register_google);
        number = findViewById(R.id.mobie_google);
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            name.setText(personName);
            email.setText(personEmail);
            Glide.with(this)
                    .load(personPhoto)
                    .placeholder(R.drawable.ic_person)
                    .into(imageView);
        }

    }

    public void loginWithGoogle(GoogleSignInAccount acct){
        if (acct == null){
            Toast.makeText(TwoStepVerificationActivity.this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
            return;
        }
        String call = number.getText().toString().trim();
        if (call.length() != 10){
            number.setError("Invalid Number");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(number);
            return;
        }
        progress.setVisibility(View.VISIBLE);
        UserData userData = new UserData();
        userData.setEmail(personEmail);
        userData.setMobile(call);
        userData.setName(personName);
        userData.setGoogle(personId);
        userData.setImage(personPhoto.toString());
        API api = RestAdapter.createAPI();
        Call<UserData> dataCall = api.registerUser(userData);
        dataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData u = response.body();
                if (u.getEmail() != null){
                    editor.putBoolean("login",true);
                    editor.apply();
                    editor.putInt("id",u.getId());
                    editor.apply();
                    startActivity(new Intent(TwoStepVerificationActivity.this,MainActivity.class));
                }else{
                    Toast.makeText(TwoStepVerificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }
}