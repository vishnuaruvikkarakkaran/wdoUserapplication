package com.weedeo.user.ui.shoplisting;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.CategoryResponseModel;
import com.weedeo.user.model.ShopListResponseModel;
import com.weedeo.user.ui.splash.SplashContract;

import java.util.List;

/**
 * Created By Athul on 04-10-2019.
 * Defines the contract between the view {@link ShopListingFragment} and the Presenter
 * {@link ShopListingPresenter}.
 */

public interface ShopListingContract {

    interface MvpView extends MvpBase {

        void onShopDataReceivedSuccess(List<ShopListResponseModel.DataBean.HitsBean> shopListData);
        void onLoadBannerListSuccess(List<String> bannerListData);
        void onLoadCategoriesSuccess(List<CategoryResponseModel.DataBean> categoryList);
    }

    interface Presenter{
        void onLoadShopList(double latitude, double longitude);
        void onLoadBannerList();
        void onLoadCategory();
    }
}
