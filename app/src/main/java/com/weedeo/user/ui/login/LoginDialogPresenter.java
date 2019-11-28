package com.weedeo.user.ui.login;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.MemberCheckRequestModel;
import com.weedeo.user.model.MemberCheckResponseModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;

/**
 * Created By Athul on 13-11-2019.
 */
public class LoginDialogPresenter implements LoginDialogContract.Presenter {

    private LoginDialogContract.MvpView mvpView;
    private Context mContext;

    public LoginDialogPresenter(LoginDialogContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
    }

    @Override
    public void onCheckNumberAlreadyPresent(String number) {
        Log.e("number : ",number);
        try {
            mvpView.onShowProgress();
            MemberCheckRequestModel memberCheckRequestModel = new MemberCheckRequestModel();
            memberCheckRequestModel.setMobile(number);
            memberCheckRequestModel.setUser_type("all");
            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String token = preferenceData.getString(KEY_ACCESS_TOKEN);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(memberCheckRequestModel));
            Call<String> call =  service.checkUserNumberExists(requestBody,token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code()== Constants.SUCCESS_CODE){
                        if (response.body()!=null){
                            Type listType = new TypeToken<MemberCheckResponseModel>() {
                            }.getType();
                            MemberCheckResponseModel memberCheckResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (memberCheckResponseModel.getStatus().equals(Constants.KEY_MEMBER_CHECK_SUCCESS))
                                mvpView.isNumberPresent(false,number);
                            else if (memberCheckResponseModel.getStatus().equals(Constants.KEY_MEMBER_CHECK_FAILED))
                                mvpView.isNumberPresent(true,number);
                        }
                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mvpView.onHideProgress();
                }
            });

        } catch (Exception e) {
            mvpView.onHideProgress();
            e.printStackTrace();
        }
    }
}
