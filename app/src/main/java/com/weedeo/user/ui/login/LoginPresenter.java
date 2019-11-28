package com.weedeo.user.ui.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.databse.DatabaseContract;
import com.weedeo.user.databse.DatabasePresenter;
import com.weedeo.user.model.LoginRequest;
import com.weedeo.user.model.LoginResponse;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;

import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_DEVICE_ID;
import static com.weedeo.user.Utils.Constants.KEY_FCM_UID;
import static com.weedeo.user.Utils.Constants.KEY_USER_EMAIL;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.KEY_USER_IMAGE;
import static com.weedeo.user.Utils.Constants.KEY_USER_NAME;
import static com.weedeo.user.Utils.Constants.KEY_USER_PHONE_NUMBER;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class LoginPresenter implements LoginContract.Presenter, DatabaseContract.MvpView {

    private LoginContract.MvpView mvpView;
    private String TAG = "LoginPresenter";
    private String fcmDeviceId;
    private DatabasePresenter databasePresenter;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0,longitude = 0;
    private Context mContext;
    private DatabaseReference mDatabase;


    LoginPresenter(LoginContract.MvpView mvpView,Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        HyperLog.i(TAG,"Failed to get fcm token");
                        Log.e("getInstanceId failed", String.valueOf(task.getException()));
                        return;
                    }

                    // Get new Instance ID token
                    fcmDeviceId = Objects.requireNonNull(task.getResult()).getToken();
                    Log.e("id", fcmDeviceId);
                });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            HyperLog.i(TAG,"successfully get location ");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("latitude : ", String.valueOf(location.getLatitude()));
                            Log.e("longitude : ", String.valueOf(location.getLongitude()));
                            // Logic to handle location object
                        }else
                            HyperLog.i(TAG,"failed to get location ");
                    }
                });
    }

    @Override
    public void onSetSliderAdapter(SliderView sliderView) {
        HyperLog.i(TAG,"onSetSliderAdapter - Executed");
        //Initializing slider Adapter
        SliderAdapter adapter = new SliderAdapter(mContext);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        //sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        /*sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });*/
    }

    @Override
    public void onUserLogin(FirebaseUser firebaseUser) {
        HyperLog.i(TAG,"onUserLogin - Executed");
        databasePresenter = new DatabasePresenter(this,mContext);
        String number = firebaseUser.getPhoneNumber();
        String id = firebaseUser.getUid();
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobile(number);
        loginRequest.setDevice_id(fcmDeviceId);
        loginRequest.setFcm_uid(id);
        loginRequest.setUser_type(Constants.USER_TYPE);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(loginRequest));
        Call<String> call =  service.getData(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    if (response.code()==Constants.SUCCESS_CODE){
                        HyperLog.i(TAG,"onUserLogin - Success");
                        Log.e("Login Request : ",new Gson().toJson(loginRequest));
                        Log.e("response : ",response.body());
                        if (response.body()!=null){
                            Type listType = new TypeToken<LoginResponse>() {
                            }.getType();
                            LoginResponse loginResponse = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (loginResponse.getStatus().equals(Constants.SUCCESS)){
                                if (loginResponse.getData()!=null){

                                    Log.e("login","response");

                                    new Thread(new Runnable() {
                                        public void run() {
                                            Log.e("login","started");
                                            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
                                            preferenceData.setString(KEY_USER_ID,loginResponse.getData().getId());
                                            preferenceData.setString(KEY_ACCESS_TOKEN,loginResponse.getToken());
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FCM_USER_TABLE_NAME);
                                            // pushing user to 'users' node using the userId
                                            mDatabase.child(loginResponse.getData().getId()).setValue(loginResponse.getData());
                                            ContentValues values = new ContentValues();
                                            values.put(KEY_USER_ID,loginResponse.getData().getId());
                                            values.put(KEY_FCM_UID,loginResponse.getData().getFcm_uid());
                                            values.put(KEY_USER_PHONE_NUMBER,loginResponse.getData().getMobile());
                                            values.put(KEY_USER_NAME,loginResponse.getData().getName());
                                            values.put(KEY_USER_EMAIL,loginResponse.getData().getEmail());
                                            values.put(KEY_DEVICE_ID,fcmDeviceId);
                                            values.put(KEY_USER_IMAGE,loginResponse.getData().getProfile_pic());
                                            values.put(KEY_ACCESS_TOKEN,loginResponse.getToken());
                                            Log.e("login","added");
                                            databasePresenter.onInsertDataIntoDatabase(values);

                                        }
                                    }).start();



                                }

                            }else
                                mvpView.onHideProgress();
                        }

                    }
                    else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){
                    }

                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    mvpView.onHideProgress();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
                HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                Log.e("Failure Message : ",t.getMessage());
            }

        });
    }

    @Override
    public void isSuccessfullyInserted(boolean status) {
        HyperLog.i(TAG,"isSuccessfullyInserted - Executed");
        HyperLog.i(TAG,"inserted status - "+status);
        if (status){
            Intent homeIntent = new Intent(mContext,NavigationDrawerActivity.class);
            if (latitude!=0 && longitude!=0){
                homeIntent.putExtra(Constants.KEY_LATITUDE,latitude);
                homeIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
            }
            mContext.startActivity(homeIntent);
            mvpView.onFinishAffinity();
            mvpView.onHideProgress();
        }else
            mvpView.onShowToast(mContext.getString(R.string.something_went_wrong_text));

    }

    @Override
    public void isDatabaseSuccessfullyUpdated(boolean status) {

    }

    @Override
    public void onDatabaseSuccessfullyDeleted() {

    }
}
