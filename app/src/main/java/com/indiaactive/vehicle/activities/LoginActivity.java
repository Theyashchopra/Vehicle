package com.indiaactive.vehicle.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.datamodels.UserData;
import com.indiaactive.vehicle.dialogs.LoginSuccessPopup;
import com.indiaactive.vehicle.interfaces.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button login,register;
    TextView notice;
    TextInputEditText username,password;
    SignInButton google;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.regnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        initialise();
        googleSigninLogic();
        onClickListeners();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void initialise(){
        google = findViewById(R.id.sign_in_button);
        login = findViewById(R.id.loginbt);
        register = findViewById(R.id.regnow);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        notice = findViewById(R.id.notice);
        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressBar = findViewById(R.id.progress);
    }

    public void googleSigninLogic(){
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.i("Login failed", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    public void onClickListeners(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(LoginActivity.this);
                validateFieldsandLogin();
                progressBar.setVisibility(View.VISIBLE);
                login.setEnabled(false);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void validateFieldsandLogin(){
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty()){
            username.setError("Username is mandatory");
            notice.setText("*Username is mandatory*");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(findViewById(R.id.emailtf));
            return;
        }else if (pass.isEmpty()){
            notice.setText("*Password is mandatory*");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(findViewById(R.id.passwordtf));
            return;
        }
        API api = RestAdapter.createAPI();
        Call<UserData> call = api.loginUser(user,pass);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData userData = response.body();
                if (userData.getEmail() != null){
                    Log.i("login",String.valueOf(userData.getId()));
                    editor.putBoolean("login",true);
                    editor.apply();
                    editor.putInt("id",userData.getId());
                    editor.apply();
                    LoginSuccessPopup lg = new LoginSuccessPopup();
                    lg.show(getSupportFragmentManager(),"login");
                }else{
                    Log.i("message",userData.getMessage());
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .playOn(findViewById(R.id.emailtf));
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .playOn(findViewById(R.id.passwordtf));
                    notice.setText("Invalid Credentials");
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account); optional
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}