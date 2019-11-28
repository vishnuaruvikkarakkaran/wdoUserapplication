package com.weedeo.user.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.CustomLogMessageFormat;

public class ApplicationClass extends Application implements Application.ActivityLifecycleCallbacks {

    private static boolean isActivityVisible;



    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FirebaseApp.initializeApp(this);
        Fresco.initialize(this);
        Stetho.initializeWithDefaults(this);
        this.registerActivityLifecycleCallbacks(this);
        HyperLog.initialize(this,new CustomLogMessageFormat(this));
        HyperLog.setLogLevel(Log.VERBOSE);
        HyperLog.setURL(Constants.LOGGING_URL);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


}
