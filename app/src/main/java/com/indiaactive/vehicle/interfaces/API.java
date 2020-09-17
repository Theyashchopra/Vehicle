package com.indiaactive.vehicle.interfaces;

import com.indiaactive.vehicle.datamodels.UserData;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @GET("user")
    Call<UserData> loginUser(@Query("username")String username,@Query("password")String password);

    @GET("user")
    Call<UserData> getProfile(@Query("id")int id);

    @POST("user")
    Call<UserData> registerUser(@Body UserData userData);

    @GET("user")
    Call<UserData> getImage(@Query("image")int id);
}
