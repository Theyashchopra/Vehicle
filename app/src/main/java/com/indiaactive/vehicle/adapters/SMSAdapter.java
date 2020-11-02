package com.indiaactive.vehicle.adapters;



import com.indiaactive.vehicle.BuildConfig;
import com.indiaactive.vehicle.interfaces.SMSPlaceholder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SMSAdapter {
    public static SMSPlaceholder createAPI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        if(BuildConfig.DEBUG){
            builder.addInterceptor(logging);
        }
        builder.cache(null);
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://103.16.142.249/api/mt/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(SMSPlaceholder.class);
    }
}
