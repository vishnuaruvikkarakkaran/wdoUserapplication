package com.weedeo.user.ui.login;

import androidx.annotation.NonNull;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hypertrack.hyperlog.HyperLog;
import com.smarteist.autoimageslider.SliderView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.ui.map.MapActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity implements LoginContract.MvpView, EasyPermissions.PermissionCallbacks, LoginDialog.LoginDialogListener,
        EasyPermissions.RationaleCallbacks{

    private String TAG = "LoginActivity";
    private LoginPresenter loginPresenter;
    private Context mContext;
    private static final String[] LOCATION_PERMISSION =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_LOCATION_PERM = 123;
    private static final int RC_LOCATION_PERM_LOGIN = 124;
    private long mLastClickTime = 0;
    private Dialog progressDialog;
    private LoginDialog loginDialog;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @BindView(R.id.imageSlider)
    SliderView sliderView;

    @BindView(R.id.setLocation)
    TextView setLocationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyperLog.i(TAG,"Activity Started");
        loginPresenter = new LoginPresenter(this,LoginActivity.this);
        loginPresenter.onSetSliderAdapter(sliderView);
        this.mContext = LoginActivity.this;
        progressDialog = AppUtils.showProgressDialog(mContext);
        HyperLog.i(TAG,"Device density - "+AppUtils.getDeviceDensity(mContext));

    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.setLocation)
    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void setLocation(){
        HyperLog.i(TAG,"Set Location Button Clicked");
        if (NetworkUtil.isConnected(this)){
            if (hasLocationPermission()){
                Log.e(TAG,"permission already graded");
                HyperLog.i(TAG,"Call Map Intent");

                Intent mapIntent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(mapIntent);

            }else {
                // Ask for permission
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.rationale_location_message),
                        RC_LOCATION_PERM,LOCATION_PERMISSION);
            }
        }else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.loginButton)
    @AfterPermissionGranted(RC_LOCATION_PERM_LOGIN)
    public void login(){
        HyperLog.i(TAG,"Set Location Button Clicked");
        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (NetworkUtil.isConnected(this)){
            if (hasLocationPermission()){
                Log.e(TAG,"permission already graded");
                HyperLog.i(TAG,"Call Map Intent");

                loginDialog = new LoginDialog(this,false);
                loginDialog.setListener(this);
                loginDialog.show();

            }else {
                // Ask for permission
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.rationale_location_message),
                        RC_LOCATION_PERM_LOGIN,LOCATION_PERMISSION);
            }

        }else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();

    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG,"permission graded");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG,"permission denied");
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    @Override
    public void onAuthenticatedSuccessfully(FirebaseUser user) {
        loginPresenter.onUserLogin(user);
    }

    @Override
    public void onFinishAffinity() {
        finishAffinity();
    }

    @Override
    public void onShowProgress() {
        if (progressDialog!=null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String message) {

    }

    @Override
    public void onCheckInternetConnection() {

    }

    @Override
    public void onRetry() {

    }

    @Override
    protected void onDestroy() {
        if (loginDialog!=null)
            loginDialog.dismiss();
        super.onDestroy();
    }
}
