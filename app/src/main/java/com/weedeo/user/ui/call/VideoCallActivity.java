package com.weedeo.user.ui.call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.bluetooth.BluetoothClass.Service.AUDIO;

public class VideoCallActivity extends BaseActivity implements VideoCallContract.MvpView,SensorEventListener,EasyPermissions.PermissionCallbacks {

    private SensorManager sensorManager;
    private Sensor proximity;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;
    private RtcEngine mRtcEngine;
    private String TAG = "VideoCallActivity";
    private VideoCallPresenter videoCallPresenter;
    private Context mContext;
    private Dialog progressDialog;
    private String agoraToken,agoraChannel;
    private boolean isUserJoined;
    private boolean mMuted;
    private boolean isCameraEnabled;
    private ToneGenerator toneGenerator;
    private String deviceId = null;
    private static final String[] CALL_PERMISSION =
            {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private static final int RC_CALL_PERM = 110;

    @BindView(R.id.remote_video_view_container)
    RelativeLayout mRemoteContainer;

    @BindView(R.id.local_video_view_container)
    FrameLayout mLocalContainer;

    @BindView(R.id.btn_call)
    ImageView endCall;

    @BindView(R.id.btn_mute)
    ImageView muteCall;

    @BindView(R.id.btn_switch_camera)
    ImageView switchCamera;

    @BindView(R.id.ringingText)
    TextView ringingTextView;

    @BindView(R.id.control_panel)
    LinearLayout controlPanel;

    @BindView(R.id.activity_video_chat_view)
    RelativeLayout containerView;

    @BindView(R.id.no_camera_view)
    RelativeLayout noCameraView;

    @BindView(R.id.remote_user_image_view)
    SimpleDraweeView remoteUserImageView;

    @BindView(R.id.remote_User_Name)
    TextView remoteUserName;


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                    if (agoraToken!=null && agoraChannel!=null){
                        ringingTextView.setText(R.string.ringing_three_dot);
                        toneGenerator = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, 100);
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, 1000000);
                        videoCallPresenter.onSendPushMessage(agoraToken,agoraChannel,deviceId,true);
                        videoCallPresenter.onControlPanelTimerStarted();
                    }

                }
            });
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of a remote user is received and decoded after the remote user successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    Toast.makeText(mContext, "Remote user joined", Toast.LENGTH_SHORT).show();
                    isUserJoined = true;
                    toneGenerator.stopTone();
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteAudio(int uid, boolean muted) {
            super.onUserMuteAudio(uid, muted);
            Log.e("agora","onUserMuteAudio " + muted);
        }



    };

    private void setupRemoteVideo(int uid) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
    }


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = VideoCallActivity.this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Ask for permission
        EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_call_message),
                RC_CALL_PERM,CALL_PERMISSION);
    }

    private void init() {
        videoCallPresenter = new VideoCallPresenter(this,mContext);
        progressDialog = AppUtils.showProgressDialog(mContext);
        ringingTextView.setText(R.string.connecting_text);
        if (getIntent().getExtras()!=null){
            if (getIntent().getStringExtra(Constants.KEY_SHOP_ID)!=null){
                videoCallPresenter.onRetrieveDeviceIdFcmDB(getIntent().getStringExtra(Constants.KEY_SHOP_ID));
                if (getIntent().getStringExtra(Constants.KEY_SHOP_IMAGE)!=null){
                    String image = Constants.SHOP_IMAGE_EXTENTION + getIntent().getStringExtra(Constants.KEY_SHOP_ID) + "/" + getIntent().getStringExtra(Constants.KEY_SHOP_IMAGE);
                    remoteUserImageView.setImageURI(Uri.parse(image));

                   /* Glide.with(this)
                            .load(image)
                            .placeholder(R.drawable.ic_user_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(remoteUserImageView);*/
                }
                if (getIntent().getStringExtra(Constants.KEY_SHOP_NAME)!=null){
                    remoteUserName.setText(getIntent().getStringExtra(Constants.KEY_SHOP_NAME));
                }
            }

        }else {
            onShowToast(getString(R.string.something_went_wrong_text));
            onFinishActivity();
        }
    }

    @OnTouch(R.id.activity_video_chat_view)
    public void onCustomTouch(){
        onShowControlPanel();
    }


    @OnClick({R.id.btn_call,R.id.btn_mute,R.id.btn_switch_camera,R.id.no_camera_view,R.id.local_video_view_container})
    public void onCustomClick(View view){

        int id = view.getId();
        switch (id){
            case R.id.btn_call:
                removeLocalVideo();
                removeRemoteVideo();
                leaveChannel();
                if (toneGenerator!=null)
                    toneGenerator.stopTone();
                break;
            case R.id.btn_mute:
                mMuted = !mMuted;
                mRtcEngine.muteLocalAudioStream(mMuted);
                int res = mMuted ? R.drawable.ic_mic_off_white_24dp : R.drawable.ic_mic_white_24dp;
                muteCall.setImageResource(res);
                break;
            case R.id.no_camera_view:
                if (mRtcEngine!=null){
                    isCameraEnabled = !isCameraEnabled;
                    mRtcEngine.enableLocalVideo(isCameraEnabled);
                    noCameraView.setVisibility(View.GONE);
                    mLocalContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.local_video_view_container:
                isCameraEnabled = !isCameraEnabled;
                mRtcEngine.enableLocalVideo(isCameraEnabled);
                noCameraView.setVisibility(View.VISIBLE);
                mLocalContainer.setVisibility(View.GONE);
                break;
            case R.id.btn_switch_camera:
                mRtcEngine.switchCamera();
                break;
        }
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }


    private void initEngineAndJoinChannel(String token, String channelName) {
        Log.e("agora channel name : ", channelName);
        Log.e("agora token : ", token);
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel(token,channelName);
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();
        mRtcEngine.enableLocalVideo(isCameraEnabled);
        mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);

        Log.e("agora", "setupVideoConfig");

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_1280x720,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    private void joinChannel(String token, String channelName) {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.

        mRtcEngine.joinChannel(token, channelName, "Extra Optional Data", 0);
    }


    private void onRemoteUserLeft() {
        removeRemoteVideo();
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
            onShowToast("Call Disconnected");
        }
        mRemoteView = null;
        finish();
    }




    @Override
    public int getLayout() {
        return R.layout.activity_video_call;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        // Do something with this sensor data.
    }

   /* @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasCallPermission()){
            videoCallPresenter.onSendPushMessage(agoraToken,agoraChannel,deviceId,false);
            leaveChannel();
            RtcEngine.destroy();
            if (toneGenerator!=null)
                toneGenerator.stopTone();
        }
    }

    private void leaveChannel() {
        if (mRtcEngine!=null)
        mRtcEngine.leaveChannel();
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
    public void onShowControlPanel() {
        controlPanel.setVisibility(View.VISIBLE);
        videoCallPresenter.onControlPanelTimerStarted();
    }

    @Override
    public void onHideControlPanel() {
        controlPanel.setVisibility(View.GONE);
    }

    @Override
    public void onDeviceIdRetrieved(String fcmId) {
        if (fcmId!=null){
            this.deviceId = fcmId;
            videoCallPresenter.onTimerStarted();
            videoCallPresenter.onGenerateToken();
        }else {
            onShowToast(getString(R.string.something_went_wrong_text));
            onFinishActivity();
        }

    }

    @Override
    public void onTokenSuccessfullyGenerated(String token, String channelName) {
        /*// Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);*/


        Toast.makeText(mContext, channelName, Toast.LENGTH_SHORT).show();
       this.agoraToken = token;
       this.agoraChannel = channelName;
       initEngineAndJoinChannel(token,channelName);

    }

    private boolean hasCallPermission() {
        return EasyPermissions.hasPermissions(mContext, CALL_PERMISSION);
    }

    @Override
    public void onFinishActivity() {
        if (toneGenerator!=null)
            toneGenerator.stopTone();
        if (!isUserJoined)
            this.finish();
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
        init();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        this.finish();
        Log.e(TAG,"permission denied");
    }
}
