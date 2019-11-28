package com.weedeo.user.ui.address.edit;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.AddressFullListResponseModel;

import java.util.List;

/**
 * Created By Athul on 18-10-2019.
 * Defines the contract between the view {@link AddressActivity} and the Presenter
 * {@link AddressPresenter}.
 */

public interface AddressContract {

    interface MvpView extends MvpBase {

        void onCheckInternetConnection();
        void onAddressListRetrieved(List<AddressFullListResponseModel.DataBean> addressList);
        void onPrimarySetSuccessfully(String addressId);
        void onAddressSuccessfullyDeleted(String addressId);
    }

    interface Presenter{
        void onGetAddress();
        void onSetPrimaryAddress(String userId, String addressId);
        void onDeleteAddress(String addressId);
    }

}
