package com.weedeo.user.data;

import com.weedeo.user.Utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static long connectTimeout = 30;
    private static long readTimeout = 10;
    private static long writeTimeout = 10;
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(chain -> {
                        Request request = chain.request().newBuilder().addHeader("header", "").build();
                        return chain.proceed(request);
                    })
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;

    }
}
