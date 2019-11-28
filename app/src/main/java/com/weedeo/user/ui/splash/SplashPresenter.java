package com.weedeo.user.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hypertrack.hyperlog.HLCallback;
import com.hypertrack.hyperlog.HyperLog;
import com.hypertrack.hyperlog.error.HLErrorResponse;
import com.weedeo.user.Utils.AESEncyption;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;
import com.weedeo.user.ui.login.LoginActivity;

import java.io.File;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.KEY_USER_PHONE_NUMBER;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class SplashPresenter implements SplashContract.Presenter{

    private SplashContract.MvpView mvpView;
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private String TAG = "SplashPresenter";
    private Context mContext;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0,longitude = 0;
    SplashPresenter(SplashContract.MvpView mvpView, Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
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
    public void onStartSplashTimer() {
        HyperLog.i(TAG,"onStartSplashTimer - Executed");
        /* New Handler to start the Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(() -> {
            HyperLog.i(TAG,"Splash screen timer completed");
            mvpView.onSplashTimerCompleted();
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void pushLogToServer() {
        HyperLog.i(TAG,"pushLogToServer - Executed");
        try {
            if (AppUtils.isUserLoggedIn(mContext)){
                SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
                File file = HyperLog.getDeviceLogsInFile(mContext);
                if (file!=null){
                    HyperLog.i(TAG,"Log file present");
                    BasicAWSCredentials credentials = new BasicAWSCredentials(AESEncyption.decrypt(Constants.UPLOAD_ENCRYPT_ACCESS_KEY),AESEncyption.decrypt(Constants.UPLOAD_ENCRYPT_SECRET_KEY));
                    AmazonS3Client s3Client = new AmazonS3Client(credentials);

                    TransferUtility transferUtility =
                            TransferUtility.builder()
                                    .context(mContext)
                                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                    .s3Client(s3Client)
                                    .build();

                    // "logs" will be the folder that contains the file
                    TransferObserver uploadObserver =
                            transferUtility.upload(Constants.LOG_FILE_PATH+preferenceData.getString(KEY_USER_ID)+"/"+file.getName(), file);

                    uploadObserver.setTransferListener(new TransferListener() {

                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (TransferState.COMPLETED == state) {
                                HyperLog.i(TAG,"Log file Upload Successfully");
                                HyperLog.deleteLogs();
                                // Handle a completed download.
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            // Handle errors
                            HyperLog.i(TAG,"Failed to upload log file : " +ex.getMessage() );
                        }

                    });
                }else
                    HyperLog.i(TAG,"No log file present");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HyperLog.i(TAG,"Log upload error : "+e.getMessage());
        }

    }

    @Override
    public void checkAutoLogin() {
        HyperLog.i(TAG,"checkAutoLogin - Executed");
        if (AppUtils.isUserLoggedIn(mContext)){
            //YOUR LOGIN CODE
            Intent homeIntent = new Intent(mContext, NavigationDrawerActivity.class);
            if (latitude!=0 && longitude!=0){
                homeIntent.putExtra(Constants.KEY_LATITUDE,latitude);
                homeIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
            }
            mContext.startActivity(homeIntent);
        }else {
            //SHOW PROMPT FOR LOGIN DETAILS
            Intent loginIntent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(loginIntent);
        }
        mvpView.onFinishActivity();
    }

}
