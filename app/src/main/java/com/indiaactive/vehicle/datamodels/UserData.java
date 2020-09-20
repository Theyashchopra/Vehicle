package com.indiaactive.vehicle.datamodels;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class UserData {
    private int id;
    private String name;
    private String email;
    private String mobile;
    @SerializedName("google_id")
    private String google;
    private String facebook;
    private String password;
    private String message;
    @SerializedName("google_image")
    private String google_image;
    private String image_url;
    public UserData(){

    }

    public void setGoogle_image(String google_image) {
        this.google_image = google_image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGoogle() {
        return google;
    }

    public String getFacebook() {
        return facebook;
    }


    public String getMessage() {
        return message;
    }

    public String getGoogle_image() {
        return google_image;
    }

    public String getImage_url() {
        return image_url;
    }
}
