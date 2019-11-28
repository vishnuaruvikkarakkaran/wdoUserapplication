package com.weedeo.user.ui.profile;

import android.net.Uri;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.UserProfileModel;

/**
 * Created By Athul on 09-10-2019.
 * Defines the contract between the view {@link ProfileActivity} and the Presenter
 * {@link ProfilePresenter}.
 */

public interface ProfileContract {

    interface MvpView extends MvpBase {
        void onSetUserImage(String path);
        void onSetUserBirthDate(String day,String month,String year);
        void onSuccessfullyLogout();
        void onUserDataReceivedSuccessfully(UserProfileModel.DataBean userData);
        void onProfileUpdatedSuccessfully(UserProfileModel.DataBean userData);
        void onReload();
        void onDismissNumberUpdateDialog();
    }

    interface Presenter{
        String onCreateProfileFileName();
        void openMediaPicker(int flag, String fileName, ProfileActivity activity);
        void onCropImage(Uri sourceUri, ProfileActivity activity);
        void onUploadImage(String path);
        void onShowDatePicker(String day, String month, String year);
        void onStartAddressActivity();
        void onUserLogout();
        void onLoadUserProfile();
        void updateProfile(String imagePath, String name, String gender, String dob, String email,String number, String fcmId);
    }

}
