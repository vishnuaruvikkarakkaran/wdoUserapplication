package com.weedeo.user.ui.NavigationDrawer;

import com.google.firebase.auth.FirebaseUser;
import com.weedeo.user.base.MvpBase;

/**
 * Created By Athul on 15-10-2019.
 * Defines the contract between the view {@link NavigationDrawerActivity} and the Presenter
 * {@link NavigationPresenter}.
 */
public interface NavigationContract {

    interface MvpView extends MvpBase {
        void setProfileImage(String imageUrl);
        void guestUser();
        void loggedInUser();
        void onCurrentLocationReceivedSuccessfully(double latitude, double longitude);
    }

    interface Presenter{
        void startProfileActivity();
        void checkUserLoginStatus();
        void getCurrentLocation();
        String getProfileImageFromDB();
        String getUserNameFromDB();
        String getUserNumberFromDB();

    }
}
