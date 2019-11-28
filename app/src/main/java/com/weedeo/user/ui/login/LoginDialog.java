package com.weedeo.user.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chaos.view.PinView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.PrefixEditText;
import com.weedeo.user.base.MvpBase;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoginDialog extends Dialog implements LoginDialogContract.MvpView {

    private Context mContext;
    private LoginDialogListener listener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private String otpCode;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private LoginDialogPresenter loginDialogPresenter;
    private boolean isNumberUpdate;

    @BindView(R.id.closeDialog)
    ImageView close;

    @BindView(R.id.phoneNumberEditText)
    PrefixEditText phoneNumberEditText;

    @BindView(R.id.continueButton)
    Button continueButton;

    @BindView(R.id.dialogTitle)
    TextView dialogTitle;

    @BindView(R.id.otpBox)
    PinView otpBox;

    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;

    @BindView(R.id.otpLayout)
    LinearLayout otpLayout;

    @BindView(R.id.timerOtp)
    TextView timerOtp;

    @BindView(R.id.resentOtp)
    TextView resentOtp;

    @BindView(R.id.confirmOtpButton)
    Button confirmOtp;

    @BindView(R.id.resentLayout)
    LinearLayout resentLayout;

    @BindView(R.id.numberTextInputLayout)
    TextInputLayout numberTextInputLayout;

    @BindView(R.id.otpTextInputLayout)
    TextInputLayout otpTextInputLayout;

    public LoginDialog(@NonNull Context context, boolean isUpdateNumber) {
        super(context);
        this.mContext = context;
        this.isNumberUpdate = isUpdateNumber;
        loginDialogPresenter =new LoginDialogPresenter(this,mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.login_dialog);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (isNumberUpdate)
            dialogTitle.setText(R.string.change_number_text);
        else
            dialogTitle.setText(R.string.login_text);

        loginLayout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);

        phoneNumberEditText.requestFocus();
        otpBox.requestFocus();


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                if (otpLayout.getVisibility()==View.VISIBLE){
                    otpBox.setText(phoneAuthCredential.getSmsCode());
                }
                Toast.makeText(context, R.string.auto_verification_text, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                listener.onHideProgress();
                Toast.makeText(mContext, "Verification Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                listener.onShowToast(mContext.getString(R.string.otp_send_sucessfully_text));
                mVerificationId = verificationId;
                mResendToken = token;
                loginLayout.setVisibility(View.GONE);
                otpLayout.setVisibility(View.VISIBLE);
                resentLayout.setVisibility(View.GONE);
                timerOtp.setVisibility(View.VISIBLE);
                dialogTitle.setText(mContext.getString(R.string.verify_otp_text));
                listener.onHideProgress();

                new CountDownTimer(120000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {
                        String time = String.format(Locale.getDefault(),"%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        timerOtp.setText(time);
                    }

                    public void onFinish() {
                        resentLayout.setVisibility(View.VISIBLE);
                        timerOtp.setVisibility(View.GONE);
                    }
                }.start();

            }
        };

    }

    @OnTextChanged(value = R.id.otpBox,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterEditTextChanged(Editable editable) {
        // Write your code here
        otpCode = editable.toString();
        if (otpCode.length()==6){
            if (otpTextInputLayout.isErrorEnabled()){
                otpTextInputLayout.setError(null);
            }
        }

    }

    @OnClick(R.id.closeDialog)
    void close(){
        dismiss();
    }

    @OnTextChanged(value = R.id.otpBox, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void otpChanged(CharSequence text) {
            if (otpTextInputLayout.isErrorEnabled()){
                otpTextInputLayout.setError(null);
            }

    }


    @OnTextChanged(value = R.id.phoneNumberEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void textChanged(CharSequence text) {

        if (text.length()==10){
            if (numberTextInputLayout.isErrorEnabled())
                numberTextInputLayout.setError(null);
        }
    }

    @OnClick(R.id.continueButton)
    void continueLogin(){

        String number = phoneNumberEditText.getText().toString().trim();
        if (phoneNumberEditText.length()==10){
            number = "+91" + number;
            if (isNumberUpdate){
                loginDialogPresenter.onCheckNumberAlreadyPresent(number);
            }else {
                //listener.onValidNumber("+91"+number);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        2,                 // Timeout duration
                        TimeUnit.MINUTES,   // Unit of timeout
                        (Activity)mContext,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
                listener.onShowProgress();
            }
        }else
            numberTextInputLayout.setError(mContext.getString(R.string.wrong_number_format_text));
            //listener.onShowToast(mContext.getString(R.string.invalid_number_text));

    }

    @OnClick(R.id.resentOtp)
    void resentOTP(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumberEditText.getText().toString(),        // Phone number to verify
                2  ,               // Timeout duration
                TimeUnit.MINUTES,   // Unit of timeout
                (Activity)mContext,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);             // Force Resending Token from callbacks
        listener.onShowProgress();
    }

    @OnClick(R.id.confirmOtpButton)
    void confirmOTP(){
        if (otpBox.getText().length()==6){
            listener.onShowProgress();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
            signInWithPhoneAuthCredential(credential);
        }else {
            otpTextInputLayout.setError(mContext.getString(R.string.mismatch_otp_text));
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity)mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            listener.onAuthenticatedSuccessfully(user);
                            Log.e("user id : ",user.getUid());
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpTextInputLayout.setError(mContext.getString(R.string.invalid_otp_text));
                                //listener.onShowToast(mContext.getString(R.string.invalid_otp_text));
                            }
                            listener.onHideProgress();
                        }

                    }
                });
    }

    public void setListener(LoginDialogListener loginDialogListener){
        this.listener = loginDialogListener;
    }

    @Override
    public void isNumberPresent(boolean isNumberExist,String number) {
        if (isNumberExist){
            listener.onHideProgress();
            numberTextInputLayout.setError(mContext.getString(R.string.number_already_used_text));
        }else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,        // Phone number to verify
                    2,                 // Timeout duration
                    TimeUnit.MINUTES,   // Unit of timeout
                    (Activity)mContext,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }

    @Override
    public void onShowProgress() {
        listener.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listener.onHideProgress();
    }


    public interface LoginDialogListener extends MvpBase {
        void onAuthenticatedSuccessfully(FirebaseUser user);
    }
}
