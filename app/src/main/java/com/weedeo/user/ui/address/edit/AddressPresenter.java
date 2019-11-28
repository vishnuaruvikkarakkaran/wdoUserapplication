package com.weedeo.user.ui.address.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.AddressFullListResponseModel;
import com.weedeo.user.model.CitySearchResponse;
import com.weedeo.user.model.PrimaryAddressModel;
import com.weedeo.user.model.request.CitySearchRequest;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.address.addnew.AddNewActivity;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class AddressPresenter implements AddressContract.Presenter {

    private AddressContract.MvpView mvpView;
    private String TAG = "SplashPresenter";
    private Context mContext;

    AddressPresenter(AddressContract.MvpView mvpView, Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
    }

    @Override
    public void onGetAddress() {
        mvpView.onShowProgress();
        SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
        String userId = preferenceData.getString(Constants.KEY_USER_ID);
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call =  service.getAddressList(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code()== Constants.SUCCESS_CODE){
                        if (response.body()!=null){
                            Log.e("response : ",response.body());
                            Type listType = new TypeToken<AddressFullListResponseModel>() {
                            }.getType();
                            AddressFullListResponseModel addressFullListResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (addressFullListResponseModel.getStatus().equals(Constants.SUCCESS)){
                                if (addressFullListResponseModel.getData()!=null){
                                    mvpView.onAddressListRetrieved(addressFullListResponseModel.getData());
                                }

                            }
                            mvpView.onHideProgress();

                        }

                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception GetAddress : ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
            }
        });
    }

    @Override
    public void onSetPrimaryAddress(String userId, String addressId) {
        mvpView.onShowProgress();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        PrimaryAddressModel primaryAddressModel = new PrimaryAddressModel();
        primaryAddressModel.setUser_id(userId);
        primaryAddressModel.setAddress_id(addressId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(primaryAddressModel));
        Call<String> call =  service.setPrimaryAddress(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    if (response.code()== Constants.SUCCESS_CODE){
                        mvpView.onHideProgress();
                        mvpView.onPrimarySetSuccessfully(addressId);
                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception onSetPrim: ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
                //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                Log.e("Failure Message : ",t.getMessage());
            }

        });
    }


    @Override
    public void onDeleteAddress(String addressId) {
        mvpView.onShowProgress();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call =  service.deleteAddress(addressId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code()== Constants.SUCCESS_CODE){

                        mvpView.onHideProgress();
                        mvpView.onAddressSuccessfullyDeleted(addressId);
                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : onDel ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
            }
        });
    }
}
