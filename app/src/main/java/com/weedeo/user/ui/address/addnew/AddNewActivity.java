package com.weedeo.user.ui.address.addnew;

import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.model.AddressFullListResponseModel;
import com.weedeo.user.model.CityResponseModel;

import org.apache.commons.text.WordUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class AddNewActivity extends BaseActivity implements AddNewContract.MvpView, StateSelectionDialog.StateSelectionDialogListener,
        CitySelectionDialog.CitySelectionDialogListener {

    private String TAG = "AddNewActivity";
    private Context mContext;
    private AddNewPresenter addNewPresenter;
    private String stateName, cityName;
    private CityResponseModel.DataBean cityModelData;
    private Dialog progressDialog;
    private String pincodeData, numberData, address1Data, address2Data, stateData, cityData, landmarkData, secondaryNumberData;
    private AddressFullListResponseModel.DataBean addressObject;
    private boolean updateState = false;
    private boolean primaryAddress = false;
    private CitySelectionDialog citySelectionDialog;
    private StateSelectionDialog stateSelectionDialog;

    @BindView(R.id.addressToolbar)
    Toolbar toolbar;

    @BindView(R.id.pinCode)
    EditText pinCodeEditText;

    @BindView(R.id.mobileNumber)
    EditText numberEditText;

    @BindView(R.id.state)
    EditText stateEditText;

    @BindView(R.id.addressLine1)
    EditText addressLine1EditText;

    @BindView(R.id.addressLine2)
    EditText addressLine2EditText;

    @BindView(R.id.city)
    EditText cityEditText;

    @BindView(R.id.landmark)
    EditText landmarkEditText;

    @BindView(R.id.name)
    EditText nameEditText;

    @BindView(R.id.secondaryNumber)
    EditText secondaryNumberEditText;

    @BindView(R.id.saveAddress)
    Button saveAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = AddNewActivity.this;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.add_address_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = AppUtils.showProgressDialog(mContext);
        addNewPresenter = new AddNewPresenter(this, mContext);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().getBooleanExtra(Constants.KEY_NEW_ADDRESS,false)){
                primaryAddress = getIntent().getBooleanExtra(Constants.KEY_NEW_ADDRESS,false);
            }


            String addressString = getIntent().getStringExtra(Constants.KEY_ADDRESS_DATA);
            if (addressString!=null){
                addressObject = new Gson().fromJson(addressString, AddressFullListResponseModel.DataBean.class);
                if (addressObject != null){
                    updateState = true;
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(getString(R.string.edit_address_title));
                    }
                    pinCodeEditText.setText(String.valueOf(addressObject.getPincode()));
                    addressLine1EditText.setText(addressObject.getAddress_line1());
                    addressLine2EditText.setText(addressObject.getAddress_line2());
                    stateEditText.setText(addressObject.getState());
                    cityEditText.setText(addressObject.getCity().getName());
                    landmarkEditText.setText(addressObject.getLand_mark());
                    nameEditText.setText(addressObject.getName());
                    numberEditText.setText(String.valueOf(addressObject.getMobile_number()));
                    secondaryNumberEditText.setText(String.valueOf(addressObject.getSecondary_number()));
                    primaryAddress = addressObject.isPrimary_status();
                }
            }
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_new;
    }


    @OnClick({R.id.state, R.id.city, R.id.saveAddress})
    public void onClick(View view) {
        if (NetworkUtil.isConnected(mContext)) {
            switch (view.getId()) {
                case R.id.state:
                    StateSelectionDialog stateSelectionDialog = new StateSelectionDialog(mContext);
                    stateSelectionDialog.setListener(this);
                    stateSelectionDialog.show();
                    break;
                case R.id.city:
                    stateName = stateEditText.getText().toString().trim();
                    if (stateName != null && stateName.length() > 0) {
                        CitySelectionDialog citySelectionDialog = new CitySelectionDialog(mContext, stateName);
                        citySelectionDialog.setListener(this);
                        citySelectionDialog.show();
                    } else
                        Toast.makeText(mContext, R.string.select_city_first_text, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.saveAddress:
                    if (validateFields()) {
                        validationSuccess();
                    }

                    break;
            }

        } else
            onShowToast(getString(R.string.no_internet));

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void validationSuccess() {
        String pincode = pinCodeEditText.getText().toString().trim();
        String address1 = WordUtils.capitalize(addressLine1EditText.getText().toString().trim());
        String address2 = WordUtils.capitalize(addressLine2EditText.getText().toString().trim());
        String state = WordUtils.capitalize(stateEditText.getText().toString().trim());
        String landmark = WordUtils.capitalize(landmarkEditText.getText().toString().trim());
        String name = WordUtils.capitalize(nameEditText.getText().toString().trim());
        String number = numberEditText.getText().toString().trim();
        String alternateNumber = secondaryNumberEditText.getText().toString().trim();
        if (updateState)
            addNewPresenter.onCreateAddressJson(name, pincode, address1, address2, state, primaryAddress, cityModelData, landmark, number, alternateNumber,addressObject.getId(),addressObject.getCity());
        else
            addNewPresenter.onCreateAddressJson(name, pincode, address1, address2, state, primaryAddress, cityModelData, landmark, number, alternateNumber,null,null);

    }

    private boolean validateFields() {
        if (AppUtils.isEmpty(pinCodeEditText)) {
            onShowToast("Enter pin code");
            return false;
        } else if (pinCodeEditText.getText().toString().trim().length() < 6) {
            onShowToast("Invalid pin code");
            return false;
        } else if (AppUtils.isEmpty(addressLine1EditText)) {
            onShowToast("Enter address");
            return false;
        } else if (AppUtils.isEmpty(addressLine2EditText)) {
            onShowToast("Enter address");
            return false;
        } else if (AppUtils.isEmpty(stateEditText)) {
            onShowToast("Select state");
            return false;
        } else if (AppUtils.isEmpty(cityEditText)) {
            onShowToast("Select city");
            return false;
        } else if (AppUtils.isEmpty(nameEditText)) {
            onShowToast("Enter your name");
            return false;
        } else if (AppUtils.isEmpty(numberEditText)) {
            onShowToast("Enter your number");
            return false;
        } else if (numberEditText.getText().toString().trim().length() < 10) {
            onShowToast("Invalid number");
            return false;
        } else
            return true;

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

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onStateSelected(CityResponseModel.DataBean state) {
        this.stateName = state.getName();
        stateEditText.setText(stateName);
        this.cityModelData = null;
        this.cityName = null;
        cityEditText.getText().clear();
    }

    @Override
    public void onCitySelected(CityResponseModel.DataBean city) {
        this.cityModelData = city;
        this.cityName = city.getName();
        cityEditText.setText(cityName);
    }


    @Override
    public void onAddressInserted() {
        primaryAddress = false;
        onShowToast(getString(R.string.message_added_successfully_text));
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

}
