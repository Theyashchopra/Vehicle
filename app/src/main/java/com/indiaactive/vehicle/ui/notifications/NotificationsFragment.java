package com.indiaactive.vehicle.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;
import com.indiaactive.vehicle.R;
import com.indiaactive.vehicle.adapters.RestAdapter;
import com.indiaactive.vehicle.datamodels.UserData;
import com.indiaactive.vehicle.dialogs.LogoutPopup;
import com.indiaactive.vehicle.interfaces.API;

import java.io.InputStream;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    ImageView profile,truck;
    View root;
    Button editbt, donebt, logoutbt;
    LogoutPopup logoutPopup;
    TextInputEditText name,email,mobile;
    String image;
    ProgressBar progressBar;
    TextInputEditText []array;
    Button [] barray;
    ImageView [] imageViews;
    int id;
    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initialise();
        logoutbt.setOnClickListener(v -> {
            logoutPopup.show(getActivity().getSupportFragmentManager(),"logout");

        });
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    private void initialise(){
        sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editbt = root.findViewById(R.id.edit);
        donebt = root.findViewById(R.id.done);
        logoutbt = root.findViewById(R.id.logout);
        logoutPopup = new LogoutPopup();
        id = sharedPreferences.getInt("id",0);
        name = root.findViewById(R.id.nameNf);
        email = root.findViewById(R.id.emailNf);
        mobile = root.findViewById(R.id.mobileNf);
        progressBar = root.findViewById(R.id.progress);
        profile = root.findViewById(R.id.profile_pic);
        truck = root.findViewById(R.id.truck);
        imageViews = new ImageView[] {profile,truck};
        barray = new Button[]{editbt,logoutbt};
        array = new TextInputEditText[] {name,email,mobile};
        try {
            animateAllStuff();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData() throws Exception{
        if (id == 0){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        API api = RestAdapter.createAPI();
        Call<UserData> call = api.getProfile(id);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData userData = response.body();
                if (userData.getEmail() != null){
                    // set texts
                    name.setText(userData.getName());
                    email.setText(userData.getEmail());
                    mobile.setText(userData.getMobile());
                    progressBar.setVisibility(View.INVISIBLE);
                    if (userData.getGoogle_image() != null){
                        Glide.with(getContext())
                                .load(userData.getGoogle_image())
                                .placeholder(R.drawable.ic_person)
                                .into(profile);
                        return;
                    }
                    try {
                        getProfilepic(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });
    }
    public void animateAllStuff() throws Exception{
        for (ImageView imageView : imageViews){
            YoYo.with(Techniques.SlideInUp)
                    .duration(2000)
                    .playOn(imageView);
        }

        for (TextInputEditText tx : array){
            YoYo.with(Techniques.SlideInUp)
                    .duration(2000)
                    .playOn(tx);
        }

        for(Button button : barray){
            YoYo.with(Techniques.SlideInUp)
                    .duration(2000)
                    .playOn(button);
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void getProfilepic(int id) throws Exception{
        API api = RestAdapter.createAPI();
        Call<ResponseBody> call = api.getImage(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    if(bmp != null){
                        profile.setImageBitmap(bmp);
                    }else {
                        profile.setImageResource(R.drawable.ic_person);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    profile.setImageResource(R.drawable.ic_person);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    profile.setImageResource(R.drawable.ic_person);
            }
        });
    }

}