package com.weedeo.user.ui.login;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.SliderView;
import com.weedeo.user.base.MvpBase;
import com.weedeo.user.ui.splash.SplashActivity;
import com.weedeo.user.ui.splash.SplashPresenter;

/**
 * Created By Athul on 09-10-2019.
 * Defines the contract between the view {@link LoginActivity} and the Presenter
 * {@link LoginPresenter}.
 */

public interface LoginContract {


    interface MvpView extends MvpBase {

        void onFinishAffinity();
    }

    interface Presenter{
        void onSetSliderAdapter(SliderView sliderView);

        void onUserLogin(FirebaseUser firebaseUser);
    }

}
