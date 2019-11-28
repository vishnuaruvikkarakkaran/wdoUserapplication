package com.weedeo.user.ui.address.edit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.Utils.NoInternetDialog;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.model.AddressFullListResponseModel;
import com.weedeo.user.ui.address.addnew.AddNewActivity;
import com.weedeo.user.ui.address.addnew.ItemDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity implements AddressContract.MvpView,AddressListAdapter.AdapterListener,
        NoInternetDialog.RetryListener {



    @BindView(R.id.addressToolbar)
    Toolbar toolbar;

    @BindView(R.id.addNewAddress)
    LinearLayout addNewAddress;

    @BindView(R.id.address_list_view)
    RecyclerView addressListView;


    private AddressPresenter addressPresenter;
    private String TAG = "AddressActivity";
    private Context mContext;
    private List<AddressFullListResponseModel.DataBean> addressList;
    private Dialog progressDialog;
    private AddressListAdapter addressListAdapter;
    public static final int REQUEST_ADDRESS_CODE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = AddressActivity.this;
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getString(R.string.address_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        addressPresenter = new AddressPresenter(this,mContext);
        progressDialog = AppUtils.showProgressDialog(mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        addressListView.setLayoutManager(mLayoutManager);
        addressListView.setItemAnimator(new DefaultItemAnimator());
        addressListView.addItemDecoration(new ItemDecorator(mContext));

        onCheckInternetConnection();
    }

    private void init(){
        addressPresenter.onGetAddress();
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
    public int getLayout() {
        return R.layout.activity_address;
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

    @OnClick(R.id.addNewAddress)
    public void onClick(){

        Intent addNewIntent = new Intent(mContext, AddNewActivity.class);
        if (addressList.size()==0){
            addNewIntent.putExtra(Constants.KEY_NEW_ADDRESS,true);
        }

        startActivityForResult(addNewIntent,REQUEST_ADDRESS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS_CODE){
            if (resultCode == RESULT_OK){
                onCheckInternetConnection();
            }
        }
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

    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String message) {

    }

    @Override
    public void onRetry() {
        init();
    }


    @Override
    public void onAddressListRetrieved(List<AddressFullListResponseModel.DataBean> list) {
        addressList = new ArrayList<>();
        addressList.addAll(list);
        addressListAdapter = new AddressListAdapter(mContext,addressList);
        addressListAdapter.setListener(this);
        addressListView.setAdapter(addressListAdapter);
    }

    @Override
    public void onPrimarySetSuccessfully(String addressId) {
        if (addressList.size()>0){
            for (int i = 0; i < addressList.size(); i++) {
                if (addressList.get(i).getId().equals(addressId))
                    addressList.get(i).setPrimary_status(true);
                else
                    addressList.get(i).setPrimary_status(false);
            }
            addressListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAddressSuccessfullyDeleted(String addressId) {
        if (addressList.size()>0){
            for (int i = 0; i < addressList.size(); i++) {
                if (addressList.get(i).getId().equals(addressId))
                    addressList.remove(i);
            }
            addressListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onShowDeleteAddressAlertDialog(String addressId) {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.delete_address_title)
                .setMessage(R.string.delete_address_message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addressPresenter.onDeleteAddress(addressId);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onItemClicked(AddressFullListResponseModel.DataBean dataBean) {
        String addressString = new Gson().toJson(dataBean);
        Intent addNewIntent = new Intent(mContext, AddNewActivity.class);
        addNewIntent.putExtra(Constants.KEY_ADDRESS_DATA,addressString);
        startActivityForResult(addNewIntent,REQUEST_ADDRESS_CODE);
    }

    @Override
    public void onSetPrimaryAddress(String userId, String addressId) {
        addressPresenter.onSetPrimaryAddress(userId,addressId);
    }

}
