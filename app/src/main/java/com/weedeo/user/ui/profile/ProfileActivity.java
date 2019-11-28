package com.weedeo.user.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.hypertrack.hyperlog.HyperLog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.Utils.NoInternetDialog;
import com.weedeo.user.Utils.TextDrawable;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.model.UserProfileModel;
import com.weedeo.user.ui.login.LoginDialog;
import com.yalantis.ucrop.UCrop;


import org.apache.commons.text.WordUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class ProfileActivity extends BaseActivity implements ProfileContract.MvpView,LoginDialog.LoginDialogListener,
                        NoInternetDialog.RetryListener{

    private String TAG = "ProfileActivity";
    private ProfilePresenter profilePresenter;
    private Context mContext;
    private Dialog progressDialog;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    private String fileName;
    private String profileImagePath;
    private boolean reload = false;
    private boolean updateStatus = false;
    private LoginDialog loginDialog;

    @BindView(R.id.user_image)
    CircularImageView userProfileImage;

    @BindView(R.id.profileToolbar)
    Toolbar toolbar;

    @BindView(R.id.userName)
    EditText userName;

    @BindView(R.id.genderSelector)
    RadioGroup genderSelector;

    @BindView(R.id.maleSelectorButton)
    RadioButton maleSelectorButton;

    @BindView(R.id.femaleSelectorButton)
    RadioButton femaleSelectorButton;

    @BindView(R.id.otherSelectorButton)
    RadioButton otherSelectorButton;

    @BindView(R.id.datePickerButton)
    LinearLayout datePicker;

    @BindView(R.id.dayTextView)
    TextView dayTextView;

    @BindView(R.id.monthTextView)
    TextView monthTextView;

    @BindView(R.id.yearTextView)
    TextView yearTextView;

    @BindView(R.id.userMobile)
    TextView userMobile;

    @BindView(R.id.userEmail)
    EditText userEmail;

    @BindView(R.id.addAddress)
    TextView addAddress;

    @BindView(R.id.userLogout)
    TextView logout;

    @BindView(R.id.address_layout)
    LinearLayout addressLayout;

    @BindView(R.id.userAddress)
    TextView userAddress;

    @BindView(R.id.updateProfile)
    Button updateProfile;

    @BindView(R.id.profile_progress_bar)
    ProgressBar profileProgressBar;

    @BindView(R.id.profile_progress_value)
    TextView profileProgressTextView;

    @BindView(R.id.name_layout)
    LinearLayout nameLayout;

    @BindView(R.id.email_layout)
    LinearLayout emailLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyperLog.i(TAG, "Activity Started");
        this.mContext = ProfileActivity.this;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_profile));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        onCheckInternetConnection();
    }

    private void init(){
        profilePresenter = new ProfilePresenter(this, mContext);
        fileName = profilePresenter.onCreateProfileFileName();
        progressDialog = AppUtils.showProgressDialog(mContext);
        profilePresenter.attachView(this);
        updateProfile.setVisibility(View.INVISIBLE);
        profilePresenter.onLoadUserProfile();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.sign_out_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes_text, (dialog, id) -> profilePresenter.onUserLogout())
                        .setNegativeButton(R.string.no_text, null)
                        .show();
                return true;
        }
        return true;
    }


    @OnTextChanged(value = {R.id.userName,R.id.userEmail},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterEditTextChanged(Editable editable) {
        if (updateStatus)
        updateProfile.setVisibility(View.VISIBLE);
    }

    @OnFocusChange({R.id.userName,R.id.userEmail})
    void onFocusChange(View view, boolean hasFocus) {
        updateStatus = true;
    }



    @OnClick({R.id.user_image, R.id.datePickerButton, R.id.addAddress, R.id.userLogout, R.id.updateProfile,R.id.maleSelectorButton,R.id.femaleSelectorButton,
    R.id.otherSelectorButton, R.id.editNumber,R.id.userName,R.id.userEmail})
    public void onClick(View view) {
        if (NetworkUtil.isConnected(mContext)) {
            switch (view.getId()) {

                case R.id.user_image:
                    Dexter.withActivity(this)
                            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        showMediaSelectionDialog();
                                    } else {
                                        // TODO - handle permission denied case
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();

                    //showMediaSelectionDialog();
                    break;

                case R.id.datePickerButton:
                    String day = dayTextView.getText().toString().trim();
                    String month = monthTextView.getText().toString().trim();
                    String year = yearTextView.getText().toString().trim();
                    profilePresenter.onShowDatePicker(day,month,year);
                    break;

                case R.id.addAddress:
                    if (updateStatus)
                        showUpdateAlertDialog(1);
                    else {
                        profilePresenter.onStartAddressActivity();
                        reload = true;
                    }
                    break;

                case R.id.userLogout:

                    break;

                case R.id.updateProfile:
                    updateProfileWithJson(null);
                    break;

                case R.id.maleSelectorButton:
                    ProfileUpdateButtonStatusChange();
                    break;

                case R.id.femaleSelectorButton:
                    ProfileUpdateButtonStatusChange();
                    break;

                case R.id.otherSelectorButton:
                    ProfileUpdateButtonStatusChange();
                    break;

                case R.id.editNumber:
                    loginDialog = new LoginDialog(mContext,true);
                    loginDialog.setListener(this);
                    loginDialog.show();
                    break;

            }

        } else
            onShowToast(getString(R.string.no_internet));
    }

    private void ProfileUpdateButtonStatusChange() {
        updateProfile.setVisibility(View.VISIBLE);
        updateStatus =true;
    }

    private void updateProfileWithJson(String fcmId) {
        String name = null;
        if (userName.getText().toString().trim().length()>0)
            name = userName.getText().toString().trim();
        String gender = null;
        if (maleSelectorButton.isChecked())
            gender = Constants.KEY_MALE;
        else if (femaleSelectorButton.isChecked())
            gender = Constants.KEY_FEMALE;
        else if (otherSelectorButton.isChecked())
            gender = Constants.KEY_OTHER;
        String dob = null;
        if (!dayTextView.getText().equals("") && !monthTextView.getText().equals("") && !yearTextView.getText().equals("")) {
            dob = dayTextView.getText().toString() + "," + monthTextView.getText().toString() + ","
                    + yearTextView.getText().toString();
        }
        String file = null;
        if (profileImagePath != null)
            file = profileImagePath;
        String email= null;
        if (userEmail.getText().toString().trim().length()>0){
            email = userEmail.getText().toString().trim();
            if (!AppUtils.isValidEmail(email)){
                Toast.makeText(mContext,R.string.invalid_email_text,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String number = null;
        if (userMobile.getText().toString().length()>0)
            number = userMobile.getText().toString().trim();

        profilePresenter.updateProfile(file, name, gender, dob,email,number,fcmId);
    }


    private void showMediaSelectionDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.media_selection_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout cameraSelector = dialog.findViewById(R.id.cameraSelector);
        cameraSelector.setOnClickListener(view -> {
            profilePresenter.openMediaPicker(Constants.CAMERA_FLAG, fileName, ProfileActivity.this);
            dialog.dismiss();
        });
        LinearLayout gallerySelector = dialog.findViewById(R.id.gallerySelector);
        gallerySelector.setOnClickListener(view -> {
            profilePresenter.openMediaPicker(Constants.GALLERY_FLAG, fileName, ProfileActivity.this);
            dialog.dismiss();
        });
        ImageView close = dialog.findViewById(R.id.closeDialog);
        close.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    profilePresenter.onCropImage(AppUtils.getCacheImagePath(mContext, fileName), ProfileActivity.this);
                    //cropImage(getCacheImagePath(fileName));
                } else {
                    //setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    profilePresenter.onCropImage(imageUri, ProfileActivity.this);
                    //cropImage(imageUri);
                } else {
                    //setResultCancelled();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //setResultCancelled();
                        return;
                    }
                    final Uri resultUri = UCrop.getOutput(data);
                    profileImagePath = resultUri.getPath();
                    profilePresenter.onUploadImage(profileImagePath);
                    //userProfileImage.setImageURI(resultUri);

                } else {
                    //setResultCancelled();
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Log.e(TAG, "Crop error: " + cropError);
                //setResultCancelled();
                break;
            default:
                //setResultCancelled();
        }


        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Uri file = AppUtils.getCacheImagePath(mContext, fileName);
                Log.e("file name 1", file.getPath());
            }

        }

    }

    @Override
    public void onSetUserImage(String path) {
        profileImagePath = path;
        updateProfileWithJson(null);
    }

    @Override
    public void onSetUserBirthDate(String day, String month, String year) {
        dayTextView.setText(day);
        monthTextView.setText(month);
        yearTextView.setText(year);
        updateProfile.setVisibility(View.VISIBLE);
        updateStatus = true;
    }

    @Override
    public void onSuccessfullyLogout() {
        HyperLog.i(TAG, "onSuccessfullyLogout-Executed");
        finishAffinity();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserDataReceivedSuccessfully(UserProfileModel.DataBean userData) {
        String image = Constants.PROFILE_IMAGE_EXTENTION + userData.getProfile_pic();
        Log.e("image path profile : ",image);
        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.default_user)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userProfileImage);
        userName.setText(userData.getName());
        userName.setFocusable(true);
        userName.setFocusableInTouchMode(true);

        if (userData.getGender() != null) {
            String gender = userData.getGender().toLowerCase();
            switch (gender) {
                case Constants.KEY_MALE:
                    maleSelectorButton.setChecked(true);
                    break;
                case Constants.KEY_FEMALE:
                    femaleSelectorButton.setChecked(true);
                    break;
                case Constants.KEY_OTHER:
                    otherSelectorButton.setChecked(true);
                    break;
            }

        }

        try {
            if (userData.getDob() != null) {
                String dob = userData.getDob();
                String[] values = dob.split(",");
                dayTextView.setText(values[0]);
                monthTextView.setText(values[1]);
                yearTextView.setText(values[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userData.getMobile() != null)
            userMobile.setText(userData.getMobile());

        if (userData.getEmail() != null){
            userEmail.setText(userData.getEmail());
            userEmail.setFocusable(true);
            userEmail.setFocusableInTouchMode(true);
           /* if (userData.isEmail_verified()){
                String verifyStatus = "Verified";
                userEmail.setCompoundDrawables(null,null,new TextDrawable(verifyStatus, Color.GREEN),null);
                Log.e("email : ", "verified");
            }
            else{
                String verifyStatus = "Not Verified";
                userEmail.setCompoundDrawables(null,null,new TextDrawable(verifyStatus, Color.RED),null);
                Log.e("email : ", "not verified");
            }*/
        }



        if (userData.getAddress() != null) {
            if (userData.getAddress().size() > 0) {
                addAddress.setText(getString(R.string.view_all_address_text));
                addressLayout.setVisibility(View.VISIBLE);
                StringBuilder builder = new StringBuilder();

                if (userData.getAddress().get(0).getAddress_line1() != null)
                    builder.append(userData.getAddress().get(0).getAddress_line1()).append(",");

                if (userData.getAddress().get(0).getAddress_line2() != null)
                    builder.append(userData.getAddress().get(0).getAddress_line2()).append(", ");

                if (userData.getAddress().get(0).getLand_mark() != null)
                    builder.append(userData.getAddress().get(0).getLand_mark()).append(", ");

                if (userData.getAddress().get(0).getCity() != null)
                    builder.append(userData.getAddress().get(0).getCity().getName()).append(", ");

                if (userData.getAddress().get(0).getState() != null)
                    builder.append(userData.getAddress().get(0).getState()).append("-");

                builder.append(userData.getAddress().get(0).getPincode()).append("\n");

                builder.append(userData.getAddress().get(0).getMobile_number());

                userAddress.setText(WordUtils.capitalize(builder.toString()));
            } else {
                addressLayout.setVisibility(View.GONE);
                addAddress.setText(getString(R.string.manage_address_text));
            }
        }

        profileProgressBar.setProgress(userData.getProfile_completion());
        profileProgressTextView.setText(userData.getProfile_completion()+"%");

    }

    @Override
    public void onProfileUpdatedSuccessfully(UserProfileModel.DataBean userData) {
        userName.clearFocus();
        userEmail.clearFocus();
        userName.setFocusable(false);
        userName.setFocusableInTouchMode(false);
        userEmail.setFocusable(false);
        userEmail.setFocusableInTouchMode(false);
        updateStatus = false;
        updateProfile.setVisibility(View.INVISIBLE);
        onUserDataReceivedSuccessfully(userData);
    }

    @Override
    public void onReload() {
        profilePresenter.onLoadUserProfile();
    }

    @Override
    public void onDismissNumberUpdateDialog() {
        if (loginDialog!=null){
            if (loginDialog.isShowing())
                loginDialog.dismiss();
        }
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
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
        if (NetworkUtil.isConnected(mContext))
            init();
        else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

    @Override
    public void onRetry() {
        init();
    }

    @Override
    protected void onDestroy() {
        if (profilePresenter != null)
            profilePresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reload) {
            reload = false;
            onCheckInternetConnection();
        }
    }

    @Override
    public void onBackPressed() {
        if (updateStatus)
        showUpdateAlertDialog(0);
        else
            finish();
    }


    private void showUpdateAlertDialog(int flag) {
        //flag 0 - from back pressed
        //flag 1 - from manage address
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.profile_update_title)
                .setMessage(R.string.profile_update_message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> updateProfileWithJson(null))

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                    if (flag==0)
                        finish();
                    else if (flag == 1){
                        profilePresenter.onStartAddressActivity();
                        reload = true;
                    }
                })
                .show();
    }

    @Override
    public void onAuthenticatedSuccessfully(FirebaseUser user) {
        profilePresenter.updateProfile(null, null, null, null,null,user.getPhoneNumber(),user.getUid());
    }
}
