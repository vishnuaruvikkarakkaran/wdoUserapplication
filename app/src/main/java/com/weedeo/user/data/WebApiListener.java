package com.weedeo.user.data;

import com.weedeo.user.model.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface WebApiListener {

    @Headers("Content-Type: application/json")
    @POST("Members/member-check")
    Call<String> getData(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("Cities/state-city")
    Call<String> getCityDetails(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("Addresses")
    Call<String> setAddressDetails(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @GET("Members/{userId}/address")
    Call<String> getAddressList(@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @POST("Addresses/set-primary")
    Call<String> setPrimaryAddress(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @PUT("Addresses/{addressId}")
    Call<String> updateAddress(@Path("addressId") String addressId, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @DELETE("Addresses/{addressId}")
    Call<String> deleteAddress(@Path("addressId") String addressId);

    @Headers("Content-Type: application/json")
    @GET("Members/{userId}?filter=%7B%22include%22%3A%5B%7B%22relation%22%3A%20%22address%22%2C%22scope%22%3A%20%7B%22where%22%3A%20%7B%20%22primary_status%22%3A%20%22true%22%20%7D%7D%7D%5D%7D")
    Call<String> loadUserProfile(@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @POST("Members/profile-update")
    Call<String> updateProfile(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("Shops/shop-search")
    Call<String> getShopList(@Body RequestBody requestBody,@Header("authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Shops/shop-list")
    Call<String> getShopListById(@Body RequestBody requestBody,@Header("authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Members/get-token")
    Call<String> getAgoraToken(@Body RequestBody requestBody,@Header("authorization") String token);

    @Headers("Content-Type: application/json")
    @POST
    Call<String> sendPushMessage(@Url String url,@Body RequestBody requestBody, @Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Members/member-exist")
    Call<String> checkUserNumberExists(@Body RequestBody requestBody, @Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("Categories/get-category")
    Call<String> getCategory(@Header("Authorization") String token);
}
