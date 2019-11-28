package com.weedeo.user.base;

public interface MvpBase {
    void onShowProgress();
    void onHideProgress();
    void onShowToast(String message);
    void onShowSnackBar(String message);
    void onShowAlertDialog(String message);
    void onCheckInternetConnection();
    void onRetry();
}
