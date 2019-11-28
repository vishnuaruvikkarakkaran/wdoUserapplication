package com.weedeo.user.ui.call;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.databse.DatabaseContract;
import com.weedeo.user.databse.DatabasePresenter;
import com.weedeo.user.model.AgoraTokenRequestModel;
import com.weedeo.user.model.AgoraTokenResponseModel;
import com.weedeo.user.model.FcmPushRequestModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.KEY_USER_IMAGE;
import static com.weedeo.user.Utils.Constants.KEY_USER_NAME;

public class VideoCallPresenter implements VideoCallContract.Presenter, DatabaseContract.MvpView {

    private String TAG = "VideoCallPresenter";
    private VideoCallContract.MvpView mvpView;
    private Context mContext;
    private DatabasePresenter databasePresenter;

    public VideoCallPresenter(VideoCallContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
        databasePresenter = new DatabasePresenter(this,mContext);
    }

    @Override
    public void onRetrieveDeviceIdFcmDB(String shopId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.FCM_ADMIN_TABLE_NAME).child(shopId).child("device_id");
// Attach a listener to read the data at our posts reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    mvpView.onDeviceIdRetrieved(dataSnapshot.getValue().toString());
                } catch (Exception e) {
                    mvpView.onDeviceIdRetrieved(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("databaseError",databaseError.getMessage());
                mvpView.onDeviceIdRetrieved(null);
            }
        });
    }

    @Override
    public void onGenerateToken() {
        try {
            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String userId = preferenceData.getString(KEY_USER_ID);
            String token = preferenceData.getString(KEY_ACCESS_TOKEN);
            String channel = userId + System.currentTimeMillis();
            AgoraTokenRequestModel requestModel = new AgoraTokenRequestModel();
            requestModel.setChannel(channel);
            requestModel.setUid("0");
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModel));
            Call<String> call =  service.getAgoraToken(requestBody,token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                Type listType = new TypeToken<AgoraTokenResponseModel>() {
                                }.getType();
                                AgoraTokenResponseModel agoraTokenResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (agoraTokenResponseModel.getStatus().equals(Constants.SUCCESS)){
                                    if (agoraTokenResponseModel.getData()!=null){
                                        Log.e("request","success");
                                        if (agoraTokenResponseModel.getData().getAccessToken()!=null)
                                            Log.e("channel token",agoraTokenResponseModel.getData().getAccessToken());
                                            mvpView.onTokenSuccessfullyGenerated(agoraTokenResponseModel.getData().getAccessToken(), channel );
                                    }

                                }else {
                                    mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));
                                }

                            }

                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                    } catch (JsonSyntaxException e) {
                        Log.e("Exception : ", e.getMessage());
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    Log.e("Failure Message : ",t.getMessage());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSendPushMessage(String token, String channelName, String deviceId, boolean callState) {
        try {
            FcmPushRequestModel fcmPushRequestModel = new FcmPushRequestModel();
            fcmPushRequestModel.setTo(deviceId);
            fcmPushRequestModel.setPriority("high");
            fcmPushRequestModel.setTime_to_live(0);
            FcmPushRequestModel.DataBean data = new FcmPushRequestModel.DataBean();
            data.setToken(token);
            data.setChannel(channelName);
            data.setUser_name(databasePresenter.getItemFromDatabase(KEY_USER_NAME));
            data.setUser_Image(databasePresenter.getItemFromDatabase(KEY_USER_IMAGE));
            data.setIs_call_active(callState);
            fcmPushRequestModel.setData(data);

            Log.e("fcm request data : ", new Gson().toJson(fcmPushRequestModel));

            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(fcmPushRequestModel));
            Call<String> call =  service.sendPushMessage(Constants.FCM_PUSH_URL,requestBody,Constants.FCM_LEGACY_KEY);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("response code : ", String.valueOf(response.code()));
                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());

                                }else {
                                    mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));
                                }

                        }
                    } catch (JsonSyntaxException e) {
                        Log.e("Exception : ", e.getMessage());
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Exception : ", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onControlPanelTimerStarted() {

        //new Handler().postDelayed(() -> mvpView.onHideControlPanel(),5 * 1000); // For 5 seconds
    }

    @Override
    public void onTimerStarted() {
        Handler handler = new Handler();
        handler.postDelayed(() -> mvpView.onFinishActivity(), 30000);
    }

    @Override
    public void isSuccessfullyInserted(boolean status) {

    }

    @Override
    public void isDatabaseSuccessfullyUpdated(boolean status) {

    }

    @Override
    public void onDatabaseSuccessfullyDeleted() {

    }
}
