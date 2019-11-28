package com.weedeo.user.ui.searchresult;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.ShopListResponseModel;
import com.weedeo.user.model.request.ShopListRequestModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.splash.SplashContract;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;

/**
 * Created By Athul on 17-11-2019.
 */
public class SearchResultPresenter implements SearchResultContract.Presenter {

    private SearchResultContract.MvpView mvpView;
    private Context mContext;

    public SearchResultPresenter(SearchResultContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
    }

    @Override
    public void onLoadShopLists(String key, double latitude, double longitude) {
        try {
            mvpView.onShowProgress();
            ShopListRequestModel requestModel = new ShopListRequestModel();
            requestModel.setFrom(0);
            requestModel.setSize(20);
            requestModel.setSearch(key);
            ShopListRequestModel.LocationBean locationBean = new ShopListRequestModel.LocationBean();
            locationBean.setLat(latitude);
            locationBean.setLon(longitude);
            requestModel.setLocation(locationBean);

            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String token = preferenceData.getString(KEY_ACCESS_TOKEN);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);

            Log.e("params search : ", new Gson().toJson(requestModel));


            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModel));
            Call<String> call =  service.getShopList(requestBody,token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                Type listType = new TypeToken<ShopListResponseModel>() {
                                }.getType();
                                ShopListResponseModel shopListResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (shopListResponseModel.getStatus().equals(Constants.SUCCESS)){
                                    if (shopListResponseModel.getData()!=null){
                                        Log.e("request","success");
                                        mvpView.shopListLoadedSuccessFully(shopListResponseModel.getData().getHits());
                                    }

                                }

                            }
                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                        mvpView.onHideProgress();
                    } catch (JsonSyntaxException e) {
                        Log.e("Exception  Search: ", e.getMessage());
                        mvpView.onHideProgress();
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    Log.e("Failure Message : ",t.getMessage());
                    mvpView.onHideProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            mvpView.onHideProgress();
        }
    }

    @Override
    public void onLoadShopListsById(String id, double latitude, double longitude) {
        try {
            mvpView.onShowProgress();
            ShopListRequestModel requestModel = new ShopListRequestModel();
            requestModel.setFrom(0);
            requestModel.setSize(20);
            requestModel.setId(id);
            ShopListRequestModel.LocationBean locationBean = new ShopListRequestModel.LocationBean();
            locationBean.setLat(latitude);
            locationBean.setLon(longitude);
            requestModel.setLocation(locationBean);

            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String token = preferenceData.getString(KEY_ACCESS_TOKEN);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);

            Log.e("params search : ", new Gson().toJson(requestModel));


            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModel));
            Call<String> call =  service.getShopListById(requestBody,token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                Type listType = new TypeToken<ShopListResponseModel>() {
                                }.getType();
                                ShopListResponseModel shopListResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (shopListResponseModel.getStatus().equals(Constants.SUCCESS)){
                                    if (shopListResponseModel.getData()!=null){
                                        Log.e("request","success");
                                        mvpView.shopListLoadedSuccessFully(shopListResponseModel.getData().getHits());
                                    }

                                }

                            }
                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                        mvpView.onHideProgress();
                    } catch (JsonSyntaxException e) {
                        Log.e("Exception  Search: ", e.getMessage());
                        mvpView.onHideProgress();
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    Log.e("Failure Message : ",t.getMessage());
                    mvpView.onHideProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            mvpView.onHideProgress();
        }
    }
}
