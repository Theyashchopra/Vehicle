package com.indiaactive.vehicle.interfaces;

import com.indiaactive.vehicle.datamodels.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("user")
    Call<UserData> loginUser(@Query("username")String username,@Query("password")String password);

    @GET("user")
    Call<UserData> getProfile(@Query("id")int id);
}
