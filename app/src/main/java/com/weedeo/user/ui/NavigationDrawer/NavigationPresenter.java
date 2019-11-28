package com.weedeo.user.ui.NavigationDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.databse.DatabaseContract;
import com.weedeo.user.databse.DatabasePresenter;
import com.weedeo.user.ui.profile.ProfileActivity;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */
public class NavigationPresenter implements NavigationContract.Presenter,DatabaseContract.MvpView{

    private String TAG = "ProfilePresenter";
    private NavigationContract.MvpView mvpView;
    private Context mContext;
    private DatabasePresenter databasePresenter;
    private FusedLocationProviderClient fusedLocationClient;

    NavigationPresenter(NavigationContract.MvpView mvpView, Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
        databasePresenter = new DatabasePresenter(this,mContext);
    }

    @Override
    public String getProfileImageFromDB() {
        HyperLog.i(TAG,"getProfileImageFromDB-Executed");
        return databasePresenter.getItemFromDatabase(Constants.KEY_USER_IMAGE);
    }

    @Override
    public String getUserNameFromDB() {
        return databasePresenter.getItemFromDatabase(Constants.KEY_USER_NAME);
    }

    @Override
    public String getUserNumberFromDB() {
        return databasePresenter.getItemFromDatabase(Constants.KEY_USER_PHONE_NUMBER);
    }


    @Override
    public void startProfileActivity() {
        HyperLog.i(TAG,"startProfileActivity-Executed");
        Intent profileIntent = new Intent(mContext, ProfileActivity.class);
        mContext.startActivity(profileIntent);
    }

    @Override
    public void checkUserLoginStatus() {
        if (AppUtils.isUserLoggedIn(mContext))
            mvpView.loggedInUser();
        else
            mvpView.guestUser();
    }

    @Override
    public void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            HyperLog.i(TAG,"successfully get location ");
                            mvpView.onCurrentLocationReceivedSuccessfully(location.getLatitude(),location.getLongitude());
                            Log.e("latitude : ", String.valueOf(location.getLatitude()));
                            Log.e("longitude : ", String.valueOf(location.getLongitude()));
                            // Logic to handle location object
                        }else
                            HyperLog.i(TAG,"failed to get location ");
                    }
                });
    }

    @Override
    public void isSuccessfullyInserted(boolean status) {

    }

    @Override
    public void isDatabaseSuccessfullyUpdated(boolean status) {

    }

    @Override
    public void onDatabaseSuccessfullyDeleted() {

    }
}
