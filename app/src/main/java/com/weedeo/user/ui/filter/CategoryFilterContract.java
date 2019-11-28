package com.weedeo.user.ui.filter;

import com.weedeo.user.base.MvpBase;
import com.weedeo.user.model.CategoryResponseModel;

public interface CategoryFilterContract {

    interface MvpView extends MvpBase {
        void onCategoryListRetrievedSuccessFully(CategoryResponseModel categoryResponseModel);
    }

    interface Presenter{
        void onGetCategoryList();
    }

}
