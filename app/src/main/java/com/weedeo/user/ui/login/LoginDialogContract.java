package com.weedeo.user.ui.login;

/**
 * Created By Athul on 13-11-2019.
 */
public interface LoginDialogContract {

    interface MvpView {
        void isNumberPresent(boolean isNumberExist, String number);
        void onShowProgress();
        void onHideProgress();
    }

    interface Presenter{
        void onCheckNumberAlreadyPresent(String number);
    }

}
