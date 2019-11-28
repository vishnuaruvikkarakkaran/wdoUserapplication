package com.weedeo.user.ui.searchresult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.Utils.NoInternetDialog;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.model.ShopListResponseModel;
import com.weedeo.user.ui.address.addnew.ItemDecorator;
import com.weedeo.user.ui.call.VideoCallActivity;
import com.weedeo.user.ui.shoplisting.ShopListAdapter;
import com.weedeo.user.ui.splash.SplashContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class SearchResultActivity extends BaseActivity implements SearchResultContract.MvpView,ShopListAdapter.ShopListAdapterListener,
                NoInternetDialog.RetryListener{

    private SearchResultPresenter searchResultPresenter;
    private Context mContext;
    private String TAG = "SearchResultActivity";
    private ShopListAdapter shopListAdapter;
    private List<ShopListResponseModel.DataBean.HitsBean> shopList;
    private String categoryId = null;
    private String categoryName = null;
    private double latitude = 0;
    private double longitude = 0;

    @BindView(R.id.searchResultToolbar)
    Toolbar toolbar;

    @BindView(R.id.searchShopListView)
    RecyclerView searchShopRecyclerview;

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SearchResultActivity.this;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (getIntent().getExtras()!=null){
            if (getIntent().getStringExtra(Constants.KEY_CATEGORY_NAME)!=null){
                if (getSupportActionBar() != null){
                    categoryName = getIntent().getStringExtra(Constants.KEY_CATEGORY_NAME);
                    getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.KEY_CATEGORY_NAME));
                }
            }
            if (getIntent().getStringExtra(Constants.KEY_CATEGORY_ID)!=null)
                categoryId = getIntent().getStringExtra(Constants.KEY_CATEGORY_ID);


            latitude = getIntent().getDoubleExtra(Constants.KEY_LATITUDE,0);
            longitude = getIntent().getDoubleExtra(Constants.KEY_LONGITUDE,0);

            Log.e("search latitude : ", String.valueOf(latitude));
            Log.e("search longitude : ", String.valueOf(longitude));
        }
        onCheckInternetConnection();
    }

    private void init(){
        searchResultPresenter = new SearchResultPresenter(this,mContext);
        if (categoryId!=null)
            searchResultPresenter.onLoadShopListsById(categoryId,latitude,longitude);
        else
            searchResultPresenter.onLoadShopLists(categoryName,latitude,longitude);
        searchShopRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        searchShopRecyclerview.setItemAnimator(new DefaultItemAnimator());
        searchShopRecyclerview.addItemDecoration(new ItemDecorator(mContext));
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_result;
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

    @Override
    public void onShowProgress() {
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onHideProgress() {
        if (mShimmerViewContainer!=null){
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
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
    public void onRetry() {
        init();
    }

    @Override
    public void shopListLoadedSuccessFully(List<ShopListResponseModel.DataBean.HitsBean> shopListData) {
            shopList = new ArrayList<>();
            shopList.addAll(shopListData);
            shopListAdapter = new ShopListAdapter(mContext,shopList);
            shopListAdapter.setListener(this);
            searchShopRecyclerview.setAdapter(shopListAdapter);
    }

    @Override
    public void onCallButtonClicked(String shopId, String shopName, String primaryImage) {
        if (AppUtils.isUserLoggedIn(mContext)){
            if (NetworkUtil.isConnected(mContext)){
                    Log.e(TAG,"permission already graded");
                    Intent callIntent = new Intent(mContext, VideoCallActivity.class);
                    callIntent.putExtra(Constants.KEY_SHOP_ID,shopId);
                    callIntent.putExtra(Constants.KEY_SHOP_NAME,shopName);
                    callIntent.putExtra(Constants.KEY_SHOP_IMAGE,primaryImage);
                    startActivity(callIntent);
            }else
                Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }else
            onShowToast("You are not logged in");
    }
}
