package com.indiaactive.vehicle.interfaces;



import com.indiaactive.vehicle.datamodels.RootMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SMSPlaceholder {

    @GET("SendSMS")
    Call<RootMessage> sendOTP(@Query("user") String username, @Query("password") String password,
                              @Query("senderid") String senderid, @Query("channel") String channel,
                              @Query("DCS") int DCS, @Query("flashsms") int flashsms, @Query("number") String number, @Query("text") String text,
                              @Query("route") int route);
}
