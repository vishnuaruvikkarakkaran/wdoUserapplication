package com.weedeo.user.ui.splash;

import android.content.Context;

import com.weedeo.user.base.MvpBase;

/**
 * Created By Athul on 04-10-2019.
 * Defines the contract between the view {@link SplashActivity} and the Presenter
 * {@link SplashPresenter}.
 */

public interface SplashContract {

    interface MvpView  {

        void onSplashTimerCompleted();
        void onFinishActivity();

    }

    interface Presenter{

        void onStartSplashTimer();
        void pushLogToServer();
        void checkAutoLogin();


    }
}
