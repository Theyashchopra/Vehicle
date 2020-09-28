package com.indiaactive.vehicle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.datamodels.UserData;
import com.indiaactive.vehicle.dialogs.RegisterSuccess;
import com.indiaactive.vehicle.imagepickers.Dialog_Get_ImageActivity;
import com.indiaactive.vehicle.imagepickers.FileUtil;
import com.indiaactive.vehicle.interfaces.API;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Dialog_Get_ImageActivity.onPhotoSelectedListener, Dialog_Get_ImageActivity.MyDialogCloseListener {

    // https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa/18052269
    // https://stackoverflow.com/questions/15659250/how-to-convert-an-image-from-url-to-hex-string
    TextView notice;
    MultipartBody.Part isImage;
    Bitmap imagebitmap, profilebitmap;
    Uri imageuri,profileuri,generalUri;
    Dialog_Get_ImageActivity dgi;
    TextInputEditText name,email,mobile,password,confirm;
    Button register;
    ProgressBar progressBar;
    ImageView imageView;
    TextInputEditText [] textInputEditTextarr;
    Object [] objects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.registerbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LoginActivity.hideKeyboard(RegisterActivity.this);
                    //register.setEnabled(false);
                    validateAndRegister();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        initialise();
        clickListeners();
    }

    private void initialise(){
        notice = findViewById(R.id.notice);
        progressBar = findViewById(R.id.progress);
        name = findViewById(R.id.nameRg);
        email = findViewById(R.id.emailRg);
        mobile = findViewById(R.id.mobileRg);
        password = findViewById(R.id.passwordRg);
        confirm = findViewById(R.id.confirmPasswordRg);
        register = findViewById(R.id.registerbt);
        imageView = findViewById(R.id.add_image);
        textInputEditTextarr = new TextInputEditText[] {name,email,mobile,password,confirm};
        try {
            animate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateAndRegister() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        UserData userData = new UserData();
        String naam = name.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String number = mobile.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conf = confirm.getText().toString().trim();
        String image = "";
        if(naam.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(name);
            name.setError("Cannot be empty");
            return;
        }else if(!isEmailValid(mail)){
            email.setError("Invalid format");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(email);
            return;
        }else if(number.length() != 10){
            mobile.setError("Invalid Number");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(mobile);
            return;
        }else if(pass.isEmpty()){
            notice.setText("*Password cannot be empty*");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(password);
            return;
        }else if(!conf.equals(pass)){
            notice.setText("*Password does not matches*");
            YoYo.with(Techniques.Shake)
                    .duration(2000)
                    .playOn(confirm);
            return;
        }
        if(generalUri != null){
            File file = FileUtil.from(this,generalUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("pic", file.getName(), requestFile);
            isImage = body;
        }
        //creating request body parameters to pass
        RequestBody fullName = RequestBody.create(naam,MediaType.parse("multipart/form-data"));
        RequestBody email_id = RequestBody.create(mail, MediaType.parse("multipart/form-data"));
        RequestBody mobile_number = RequestBody.create(number,MediaType.parse("multipart/form-data"));
        RequestBody password = RequestBody.create(pass, MediaType.parse("multipart/form-data"));
        Call<UserData> call;
        if(isImage != null) {
            call = RestAdapter.createAPI().registerUser(fullName, email_id, password, mobile_number, isImage);
        }else{
            File file = File.createTempFile("temp","jpg");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("pic", file.getName(), requestFile);
            call = RestAdapter.createAPI().registerUser(fullName,email_id,password,mobile_number,body);

        }
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData u = response.body();
                if(response.isSuccessful()) {
                    if (u.getEmail() != null) {
                        RegisterSuccess rg = new RegisterSuccess();
                        rg.show(getSupportFragmentManager(), "success");
                    } else {
                        notice.setText(u.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    //animate all assets
    public void animate() throws Exception{
        for(TextInputEditText textInputEditText : textInputEditTextarr){
            YoYo.with(Techniques.SlideInUp)
                    .duration(2000)
                    .playOn(textInputEditText);
        }
        YoYo.with(Techniques.SlideInUp)
                .duration(2000)
                .playOn(register);
        YoYo.with(Techniques.SlideInUp)
                .duration(2000)
                .playOn(imageView);
    }

    @Override
    public void getImagePath(Uri imagePath) {
        imageuri = imagePath;
        imagebitmap = null;
        profileuri = imageuri;
        profilebitmap = null;
        generalUri = imageuri;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        imagebitmap = bitmap;
        imageuri = null;
        profilebitmap = imagebitmap;
        profileuri = null;
        generalUri = getImageUri(this,imagebitmap);
    }

    @Override
    public void handleDialogClose(int num) {
        if(imagebitmap != null){
            imageView.setImageBitmap(imagebitmap);
        }else if(imageuri != null){
            imageView.setImageURI(imageuri);
        }
    }

    private void clickListeners(){
        imageView.setOnClickListener(view -> {
            Bundle ags=new Bundle();
            ags.putInt("curr",1);
            dgi = new Dialog_Get_ImageActivity();
            dgi.setArguments(ags);
            dgi.show(getSupportFragmentManager(),"Dialog_select_Image");
            Log.e("Image Adder","----"+1);
        });

    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //convert bitmap to string
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //get uri of bitmap
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}