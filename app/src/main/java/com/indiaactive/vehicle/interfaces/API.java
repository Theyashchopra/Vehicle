package com.indiaactive.vehicle.interfaces;

import androidx.annotation.Nullable;

import com.indiaactive.vehicle.datamodels.UserData;

import java.io.File;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {

    @GET("user")
    Call<UserData> loginUser(@Query("username")String username,@Query("password")String password);

    @GET("user")
    Call<UserData> getProfile(@Query("id")int id);

    @Multipart
    @POST("user")
    Call<UserData> registerUser(@Part("name") RequestBody name,
                                @Part("email")RequestBody email, @Part("password")RequestBody password,
                                @Part("mobile")RequestBody mobile, @Part MultipartBody.Part image);

    @Multipart
    @POST("user")
    Call<UserData> registerUserWithoutImage(@Part("name") RequestBody name,
                                            @Part("email")RequestBody email, @Part("password")RequestBody password,
                                            @Part("mobile")RequestBody mobile, @Nullable @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("user")
    Call<UserData> registerGoogleUser(@Field("name") String name,@Field("email")String email,
                                      @Field("mobile")String mobile,@Field("google_id")String google_id,
                                      @Field("google_image")String image);

    @GET("user")
    Call<ResponseBody> getImage(@Query("image")int id);

    @GET("user")
    Call<UserData> getGoogleUser(@Query("google") String google);


}
