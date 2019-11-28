package com.weedeo.user.ui.call;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.ui.login.LoginActivity;
import com.weedeo.user.ui.login.LoginPresenter;

/**
 * Created By Athul on 06-11-2019.
 * Defines the contract between the view {@link VideoCallActivity} and the Presenter
 * {@link VideoCallPresenter}.
 */
public interface VideoCallContract {

    interface MvpView extends MvpBase {
        void onShowControlPanel();
        void onHideControlPanel();
        void onDeviceIdRetrieved(String deviceId);
        void onTokenSuccessfullyGenerated(String token, String channelName);
        void onFinishActivity();
    }

    interface Presenter{
        void onRetrieveDeviceIdFcmDB(String shopId);
        void onGenerateToken();
        void onSendPushMessage(String token, String channelName, String deviceId, boolean callState);
        void onControlPanelTimerStarted();
        void onTimerStarted();
    }

}
