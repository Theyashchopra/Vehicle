package com.indiaactive.vehicle.datamodels;

public class UserData {
    private int id;
    private String name;
    private String email;
    private String mobile;
    private String google;
    private String facebook;
    private String image;
    private String password;
    private String message;
    public UserData(){

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

    public void setImage(String image) {
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }
}
