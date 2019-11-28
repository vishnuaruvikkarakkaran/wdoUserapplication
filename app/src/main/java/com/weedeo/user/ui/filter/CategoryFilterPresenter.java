package com.weedeo.user.ui.filter;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.CategoryResponseModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;

public class CategoryFilterPresenter implements CategoryFilterContract.Presenter {

    private CategoryFilterContract.MvpView mvpView;
    private Context mContext;

    public CategoryFilterPresenter(CategoryFilterContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
    }

    @Override
    public void onGetCategoryList() {
        try {
            mvpView.onShowProgress();
            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String token = preferenceData.getString(KEY_ACCESS_TOKEN);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            Call<String> call = service.getCategory(token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == Constants.SUCCESS_CODE) {
                        if (response.body() != null) {
                            Log.e("response : ", response.body());
                            Type listType = new TypeToken<CategoryResponseModel>() {
                            }.getType();
                            CategoryResponseModel categoryResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            mvpView.onCategoryListRetrievedSuccessFully(categoryResponseModel);
                        } else {
                            mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));
                        }

                    } else if (response.code() == Constants.ERROR_CODE) {
                    } else if (response.code() == Constants.ERROR_BAD_GATEWAY_CODE) {
                    } else if (response.code() == Constants.ERROR_UNAUTHORIZED_CODE) {

                    }

                    mvpView.onHideProgress();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mvpView.onHideProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mvpView.onHideProgress();
        }
    }
}
