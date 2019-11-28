package com.weedeo.user.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.DatePicker;

import androidx.core.content.ContextCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AESEncyption;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.MessageEvent;
import com.weedeo.user.base.BasePresenter;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.databse.DatabaseContract;
import com.weedeo.user.databse.DatabasePresenter;
import com.weedeo.user.model.ProfileUpdateModel;
import com.weedeo.user.model.UserProfileModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.address.edit.AddressActivity;
import com.weedeo.user.ui.login.LoginActivity;
import com.yalantis.ucrop.UCrop;

import org.apache.commons.text.WordUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.AppUtils.rename;
import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_DEVICE_ID;
import static com.weedeo.user.Utils.Constants.KEY_FCM_UID;
import static com.weedeo.user.Utils.Constants.KEY_USER_EMAIL;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;
import static com.weedeo.user.Utils.Constants.KEY_USER_IMAGE;
import static com.weedeo.user.Utils.Constants.KEY_USER_NAME;
import static com.weedeo.user.Utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.weedeo.user.ui.profile.ProfileActivity.REQUEST_GALLERY_IMAGE;
import static com.weedeo.user.ui.profile.ProfileActivity.REQUEST_IMAGE_CAPTURE;

/**
 * Responsible for handling actions from the view and updating the UI
 * as required.
 */

public class ProfilePresenter extends BasePresenter implements ProfileContract.Presenter, DatabaseContract.MvpView {

    private String TAG = "ProfilePresenter";
    private ProfileContract.MvpView mvpView;
    private Context mContext;
    private DatabasePresenter databasePresenter;
    private DatabaseReference mDatabase;
    private UserProfileModel.DataBean userData;

    ProfilePresenter(ProfileContract.MvpView mvpView, Context context) {
        this.mvpView = mvpView;
        this.mContext = context;
        databasePresenter = new DatabasePresenter(this,mContext);
    }

    @Override
    public String onCreateProfileFileName() {
        SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
        return  preferenceData.getString(KEY_USER_ID)+".jpg";
    }

    @Override
    public void openMediaPicker(int flag, String fileName, ProfileActivity activity) {
        if (flag==Constants.CAMERA_FLAG){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, AppUtils.getCacheImagePath(mContext,fileName));
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else{
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
        }
    }

    @Override
    public void onCropImage(Uri sourceUri, ProfileActivity activity) {
        Uri destinationUri = Uri.fromFile(new File(mContext.getCacheDir(), AppUtils.queryName(mContext.getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(Constants.IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.white));
        options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.black));
        options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.white));
        options.setActiveWidgetColor(ContextCompat.getColor(mContext, R.color.black));
        options.withAspectRatio(Constants.PROFILE_ASPECT_RATIO_X, Constants.PROFILE_ASPECT_RATIO_Y);
        options.withMaxResultSize(500, 500);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(activity);
    }

    @Override
    public void onUploadImage(String path) {
        try {

            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            File currentFile = new File(path);
            if (currentFile.exists()) {
                HyperLog.i(TAG, "image file present");
                try {
                    File newFile = new File(mContext.getCacheDir() , preferenceData.getString(KEY_USER_ID) + Constants.EXTENSION_PNG);
                    if (rename(currentFile, newFile)) {
                        if (newFile.exists()) {
                            Log.e("profile","file renamed");
                            uploadFileToS3(newFile);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                HyperLog.i(TAG, "No image file present");
                mvpView.onHideProgress();
            }
        } catch (Exception e) {
            HyperLog.i(TAG, e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onShowDatePicker(String day, String month, String year) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear,mMonth,mDay;
        if (day!=null && month!=null && year!=null){
            if (day.equals("") && month.equals("") && year.equals("")){
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            }else {
                mYear = Integer.parseInt(year);
                mMonth = AppUtils.getMonthIneger(month);
                mDay = Integer.parseInt(day);
            }
        }else {
             mYear = c.get(Calendar.YEAR);
             mMonth = c.get(Calendar.MONTH);
             mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year1, monthOfYear, dayOfMonth) -> mvpView.onSetUserBirthDate(String.valueOf(dayOfMonth),AppUtils.getMonth(monthOfYear),String.valueOf(year1)), mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(AppUtils.reduceDateByYear(10));
        datePickerDialog.show();
    }

    @Override
    public void onStartAddressActivity() {
        Intent addressIntent = new Intent(mContext, AddressActivity.class);
        mContext.startActivity(addressIntent);

    }

    @SuppressLint("StaticFieldLeak")
    private void uploadFileToS3(File newFile) {

        Log.e("profile","start upload");


        new AsyncTask<Void, Void, Void>() {

            private Dialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                        progressDialog = AppUtils.showProgressDialog(mContext);
                        progressDialog.show();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    BasicAWSCredentials credentials = new BasicAWSCredentials(AESEncyption.decrypt(Constants.UPLOAD_ENCRYPT_ACCESS_KEY),AESEncyption.decrypt(Constants.UPLOAD_ENCRYPT_SECRET_KEY));
                    AmazonS3Client s3Client = new AmazonS3Client(credentials);

                    TransferUtility transferUtility =
                            TransferUtility.builder()
                                    .context(mContext)
                                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                    .s3Client(s3Client)
                                    .build();

                    TransferObserver uploadObserver =
                            transferUtility.upload(Constants.PROFILE_IMAGE_PATH_S3+newFile.getName(), newFile);
                    uploadObserver.setTransferListener(new TransferListener() {


                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (TransferState.COMPLETED == state) {
                                if (isViewAttached()){
                                    mvpView.onSetUserImage(newFile.getPath());
                                    progressDialog.dismiss();
                                    HyperLog.i(TAG,"Log file Upload Successfully");
                                    Log.e("path : ",new Gson().toJson(state));
                                }

                                // Handle a completed download.

                            }else if (TransferState.FAILED == state){
                                if (isViewAttached())
                                    progressDialog.dismiss();
                            }else if(TransferState.CANCELED == state){
                                if (isViewAttached())
                                    progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            Log.e("progress : ", String.valueOf(id));
                            int percent = (int)(100.0*(double)bytesCurrent/bytesTotal + 0.5);
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            // Handle errors
                            HyperLog.i(TAG,"Failed to upload log file : " +ex.getMessage() );
                            progressDialog.dismiss();
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //mvpView.onHideProgress();
                super.onPostExecute(aVoid);
            }
        }.execute();



       /* mvpView.onShowProgress();
        new Thread(new Runnable() {
            public void run() {

                BasicAWSCredentials credentials = new BasicAWSCredentials(Constants.AWS_ACCESS_KEY, Constants.AWS_SECRET_KEY);
                AmazonS3Client s3Client = new AmazonS3Client(credentials);

                TransferUtility transferUtility =
                        TransferUtility.builder()
                                .context(mContext)
                                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                .s3Client(s3Client)
                                .build();

                TransferObserver uploadObserver =
                        transferUtility.upload(Constants.PROFILE_IMAGE_PATH_S3+newFile.getName(), newFile);
                uploadObserver.setTransferListener(new TransferListener() {


                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (TransferState.COMPLETED == state) {
                            Log.e("profile","complete upload");
                            if (isViewAttached()){
                                mvpView.onSetUserImage(newFile.getPath());
                                HyperLog.i(TAG,"Log file Upload Successfully");
                                Log.e("path : ",new Gson().toJson(state));
                            }

                            // Handle a completed download.

                        }
                        if (isViewAttached())
                            mvpView.onHideProgress();
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        Log.e("profile img progress : ", String.valueOf(bytesCurrent));
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        // Handle errors
                        HyperLog.i(TAG,"Failed to upload log file : " +ex.getMessage() );
                        mvpView.onHideProgress();
                    }

                });
            }
        }).start();*/


    }

    @Override
    public void isSuccessfullyInserted(boolean status) {

    }

    @Override
    public void isDatabaseSuccessfullyUpdated(boolean status) {
        new Handler(Looper.getMainLooper()).post(() -> {
            mvpView.onHideProgress();
            mvpView.onProfileUpdatedSuccessfully(userData);
            mvpView.onDismissNumberUpdateDialog();
            mvpView.onShowToast(mContext.getString(R.string.update_successfully));
        });

    }

    @Override
    public void onDatabaseSuccessfullyDeleted() {
        HyperLog.i(TAG,"onDatabaseSuccessfullyDeleted-Executed");
        FirebaseAuth.getInstance().signOut();
        SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(mContext);
        sharedPreferenceData.deleteAllPreference();
        Intent loginIntent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(loginIntent);
        mvpView.onSuccessfullyLogout();
        mvpView.onHideProgress();
        mvpView.onShowToast(mContext.getString(R.string.logout_successfully_text));
    }

    @Override
    public void onUserLogout() {
        HyperLog.i(TAG,"onUserLogout-Executed");
        mvpView.onShowProgress();
        databasePresenter.deleteDatabaseValues();
    }

    @Override
    public void onLoadUserProfile() {
        mvpView.onShowProgress();
        SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
        String userId = preferenceData.getString(Constants.KEY_USER_ID);
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        Call<String> call =  service.loadUserProfile(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code()== Constants.SUCCESS_CODE){
                        if (response.body()!=null){
                            Log.e("response : ",response.body());
                            Type listType = new TypeToken<UserProfileModel>() {
                            }.getType();
                            UserProfileModel userProfileModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (userProfileModel.getStatus().equals(Constants.SUCCESS)){
                                if (userProfileModel.getData()!=null){
                                    mvpView.onUserDataReceivedSuccessfully(userProfileModel.getData());
                                }

                            }
                            mvpView.onHideProgress();

                        }

                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onHideProgress();
            }
        });
    }

    @Override
    public void updateProfile(String imagePath, String name, String gender, String dob, String email, String number, String fcmUid) {

        //EventBus.getDefault().post(new MessageEvent(name,gender,email));

        if (isViewAttached()){
            mvpView.onShowProgress();
            SharedPreferenceData preferenceData = new SharedPreferenceData(mContext);
            String userId = preferenceData.getString(KEY_USER_ID);
            WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
            ProfileUpdateModel profileUpdateModel = new ProfileUpdateModel();
            profileUpdateModel.setUser_id(userId);
            profileUpdateModel.setUser_type(Constants.USER_TYPE);
            if (imagePath!=null){
                profileUpdateModel.setProfile_pic(imagePath.substring(imagePath.lastIndexOf("/")+1));
                //Updating user fields updating event
                preferenceData.setBool(Constants.KEY_IS_PROFILE_UPDATE,true);
            }

            if (name!=null)
                profileUpdateModel.setName(WordUtils.capitalize(name));
            else
                profileUpdateModel.setName("");
            if (gender!=null)
                profileUpdateModel.setGender(gender);
            if (dob!=null)
                profileUpdateModel.setDob(dob);
            if (email!=null)
                profileUpdateModel.setEmail(email);
            else
                profileUpdateModel.setEmail("");
            if (number!=null)
                profileUpdateModel.setMobile(number);
            if (fcmUid!=null)
                profileUpdateModel.setFcm_uid(fcmUid);

            Log.e("profile request : ",new Gson().toJson(profileUpdateModel));

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(profileUpdateModel));
            Call<String> call =  service.updateProfile(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        if (response.code()== Constants.SUCCESS_CODE){
                            if (response.body()!=null){
                                Log.e("response : ",response.body());
                                Type listType = new TypeToken<UserProfileModel>() {
                                }.getType();
                                UserProfileModel userProfileModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (userProfileModel.getStatus().equals(Constants.SUCCESS)){
                                    if (userProfileModel.getData()!=null){
                                        /*String name = null,phone = null,image =null;
                                        if (userProfileModel.getData().getName()!=null)
                                            name = userProfileModel.getData().getName();
                                        if (userProfileModel.getData().getMobile()!=null)
                                            phone = userProfileModel.getData().getMobile();
                                        if (userProfileModel.getData().getProfile_pic()!=null)
                                            image = userProfileModel.getData().getProfile_pic();

                                        EventBus.getDefault().post(new MessageEvent(name,phone,image));*/

                                        userData = userProfileModel.getData();

                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                        // pushing user to 'users' node using the userId
                                        mDatabase.child(userProfileModel.getData().getId()).setValue(userProfileModel.getData());

                                        ContentValues values = new ContentValues();
                                        //values.put(KEY_USER_ID,loginResponse.getData().getId());
                                        values.put(KEY_FCM_UID,userProfileModel.getData().getFcm_uid());
                                        values.put(KEY_USER_PHONE_NUMBER,userProfileModel.getData().getMobile());
                                        values.put(KEY_USER_NAME,userProfileModel.getData().getName());
                                        values.put(KEY_USER_EMAIL,userProfileModel.getData().getEmail());
                                        //values.put(KEY_DEVICE_ID,fcmDeviceId);
                                        values.put(KEY_USER_IMAGE,userProfileModel.getData().getProfile_pic());
                                       // values.put(KEY_ACCESS_TOKEN,loginResponse.getToken());
                                        Log.e("login","added");
                                        databasePresenter.onUpdateUserData(values,userProfileModel.getData().getId());

                                    }

                                }

                            }

                        }else if (response.code()==Constants.ERROR_CODE){
                        }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                        }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                        }
                    } catch (JsonSyntaxException e) {
                        Log.e("Exception : ", e.getMessage());
                        // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                    Log.e("Failure Message : ",t.getMessage());
                }

            });
        }


    }
}
