package com.weedeo.user.ui.searchresult;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.ShopListResponseModel;

import java.util.List;

/**
 * Created By Athul on 17-11-2019.
 */
public interface SearchResultContract {

    interface MvpView extends MvpBase {
        void shopListLoadedSuccessFully(List<ShopListResponseModel.DataBean.HitsBean> shopListData);
    }

    interface Presenter{
        void onLoadShopLists(String key, double latitude, double longitude);
        void onLoadShopListsById(String id, double latitude, double longitude);
    }
}

