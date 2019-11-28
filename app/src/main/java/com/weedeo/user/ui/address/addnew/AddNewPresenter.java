package com.weedeo.user.ui.address.addnew;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.AddressFullListResponseModel;
import com.weedeo.user.model.AddressModel;
import com.weedeo.user.model.CityResponseModel;
import com.weedeo.user.model.CitySearchResponse;
import com.weedeo.user.model.request.CitySearchRequest;
import com.weedeo.user.sharedpreference.SharedPreferenceData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_USER_ID;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class AddNewPresenter implements AddNewContract.Presenter {

    private AddNewContract.MvpView mvpView;
    private String TAG = "AddNewPresenter";
    private Context mContext;

    AddNewPresenter(AddNewContract.MvpView mvpView, Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
    }

    @Override
    public void onCreateAddressJson(String name, String pinCode, String address1, String address2, String state, boolean primaryStatus, CityResponseModel.DataBean city, String landmark, String number, String secondaryNumber,
                                    String addressId, AddressFullListResponseModel.DataBean.CityBean cityObject) {

        SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
        String userId = preferenceData.getString(KEY_USER_ID);

        AddressModel.CityBean cityData = new AddressModel.CityBean();
        if (cityObject==null){
            cityData.setName(city.getName());
            cityData.setId(city.getId());
            cityData.setCreatedAt(city.getCreatedAt());
            cityData.setState(city.getState());
            cityData.setLatitude(city.getLatitude());
            cityData.setLongitude(city.getLongitude());
            cityData.setStatus(city.getStatus());
        }else {

            if (city!=null){
                cityData.setName(city.getName());
                cityData.setId(city.getId());
                cityData.setCreatedAt(city.getCreatedAt());
                cityData.setState(city.getState());
                cityData.setLatitude(city.getLatitude());
                cityData.setLongitude(city.getLongitude());
                cityData.setStatus(city.getStatus());
            }else {
                cityData.setName(cityObject.getName());
                cityData.setId(cityObject.getId());
                cityData.setCreatedAt(cityObject.getCreatedAt());
                cityData.setState(cityObject.getState());
                cityData.setLatitude(String.valueOf(cityObject.getLatitude()));
                cityData.setLongitude(String.valueOf(cityObject.getLongitude()));
                cityData.setStatus(String.valueOf(cityObject.getStatus()));
            }

        }

        AddressModel addressData = new AddressModel();
        addressData.setName(name);
        addressData.setPincode(pinCode);
        addressData.setAddress_line1(address1);
        addressData.setAddress_line2(address2);
        addressData.setState(state);
        addressData.setCity(cityData);
        addressData.setLand_mark(landmark);
        addressData.setMobile_number(number);
        addressData.setSecondary_number(secondaryNumber);
        addressData.setUser_id(userId);
        addressData.setPrimary_status(primaryStatus);

        submitAddress(addressData,addressId);

    }

    private void submitAddress(AddressModel addressData, String addressId) {
        mvpView.onShowProgress();

        if (addressId==null){
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(addressData));
            Call<String> call =  service.setAddressDetails(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                mvpView.onHideProgress();
                                mvpView.onAddressInserted();
                            }

                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                    } catch (JsonSyntaxException e) {
                        mvpView.onHideProgress();
                        Log.e("Exception : ", e.getMessage());
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    mvpView.onHideProgress();
                    Log.e("Failure Message : ",t.getMessage());
                }

            });
        }else {
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(addressData));
            Call<String> call =  service.updateAddress(addressId,requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                mvpView.onHideProgress();
                                mvpView.onAddressInserted();
                            }

                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                    } catch (JsonSyntaxException e) {
                        mvpView.onHideProgress();
                        Log.e("Exception : ", e.getMessage());
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    mvpView.onHideProgress();
                    Log.e("Failure Message : ",t.getMessage());
                }

            });
        }

    }
}