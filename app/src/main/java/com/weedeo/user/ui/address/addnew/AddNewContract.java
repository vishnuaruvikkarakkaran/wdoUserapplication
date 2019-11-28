package com.weedeo.user.ui.address.addnew;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.AddressFullListResponseModel;
import com.weedeo.user.model.AddressModel;
import com.weedeo.user.model.CityResponseModel;
import com.weedeo.user.model.CitySearchResponse;
import com.weedeo.user.ui.address.edit.AddressPresenter;

import java.util.List;

/**
 * Created By Athul on 18-10-2019.
 * Defines the contract between the view {@link AddNewActivity} and the Presenter
 * {@link AddNewPresenter}.
 */

public interface AddNewContract {

        interface MvpView extends MvpBase {

                void onAddressInserted();
        }

        interface Presenter{
                void onCreateAddressJson(String name, String pinCode, String address1, String address2, String state, boolean primaryStatus, CityResponseModel.DataBean city, String landmark,
                                         String number, String secondaryNumber, String addressId, AddressFullListResponseModel.DataBean.CityBean cityObject);
        }

}
