package com.weedeo.user.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AESEncyption;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;
import com.weedeo.user.ui.login.LoginActivity;

import java.io.File;

public class SplashActivity extends BaseActivity implements SplashContract.MvpView {

    private SplashPresenter splashPresenter;
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyperLog.i(TAG,"Activity Started");
        AWSMobileClient.getInstance().initialize(this).execute();
        splashPresenter = new SplashPresenter(this,SplashActivity.this);
        splashPresenter.pushLogToServer();
        splashPresenter.onStartSplashTimer();

    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onSplashTimerCompleted() {
        HyperLog.i(TAG,"onSplashTimerCompleted - Executed");
        splashPresenter.checkAutoLogin();
    }

    @Override
    public void onFinishActivity() {
        this.finish();
    }
}
