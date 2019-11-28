package com.weedeo.user.ui.shoplisting;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.GridSpacingItemDecoration;
import com.weedeo.user.Utils.ItemOffsetDecoration;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.Utils.NoInternetDialog;
import com.weedeo.user.Utils.SpacesItemDecoration;
import com.weedeo.user.base.BaseFragment;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.CategoryResponseModel;
import com.weedeo.user.model.ShopListResponseModel;
import com.weedeo.user.model.request.ShopListRequestModel;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;
import com.weedeo.user.ui.address.addnew.ItemDecorator;
import com.weedeo.user.ui.call.VideoCallActivity;
import com.weedeo.user.ui.filter.CategoryFilterActivity;
import com.weedeo.user.ui.searchresult.SearchResultActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.weedeo.user.Utils.Constants.KEY_ACCESS_TOKEN;
import static com.weedeo.user.Utils.Constants.KEY_USER_ID;


public class ShopListingFragment extends BaseFragment implements ShopListingContract.MvpView, ShopListAdapter.ShopListAdapterListener,
        CategoryHomeAdapter.AdapterListener, NoInternetDialog.RetryListener{


    private ShopListingPresenter shopListingPresenter;
    private Context mContext;
    private String TAG = "ShopListingFragment";
    private Dialog progressDialog;
    private List<ShopListResponseModel.DataBean.HitsBean> shopList;
    private ShopListAdapter shopListAdapter;
    private BannerAdapter bannerAdapter;
    private CategoryHomeAdapter categoryHomeAdapter;
    private BannerPagerAdapter bannerPagerAdapter;
    private double latitude = 0;
    private double longitude = 0;


    /*@BindView(R.id.bannerViewPager)
    ViewPager bannerPagerView;*/

    @BindView(R.id.bannerView)
    RecyclerView bannerRecyclerView;

    @BindView(R.id.shopListView)
    RecyclerView shopListRecyclerView;

    @BindView(R.id.categoryView)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    public ShopListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mContext = getActivity();
        setHasOptionsMenu(true);
        onCheckInternetConnection();
    }


    private void init(){
        progressDialog = AppUtils.showProgressDialog(mContext);
        shopListingPresenter = new ShopListingPresenter(this,mContext);
        shopListingPresenter.onLoadBannerList();
        Bundle args = getArguments();
        if (args!=null){
            latitude = args.getDouble(Constants.KEY_LATITUDE);
            longitude = args.getDouble(Constants.KEY_LONGITUDE);
        }
        shopListingPresenter.onLoadShopList(latitude,longitude);
        shopListingPresenter.onLoadCategory();

        shopListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        shopListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        shopListRecyclerView.addItemDecoration(new ItemDecorator(mContext));

        bannerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        bannerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bannerRecyclerView.addItemDecoration(new ItemOffsetDecoration(10));

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.addItemDecoration(new ItemOffsetDecoration(10));


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(bannerRecyclerView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop_listing;
    }

    /*@OnClick(R.id.call)
    public void onClick(){
        Toast.makeText(getActivity(), "Initiate Call", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(getActivity(), VideoCallActivity.class);
        startActivity(callIntent);
    }*/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem filterItem = menu.findItem(R.id.filter_menu);
        if (filterItem!=null)
            filterItem.setVisible(false);
        MenuItem actionSearchItem =  menu.findItem(R.id.action_search);
        if (actionSearchItem!=null)
            actionSearchItem.setVisible(false);
    }

    @Override
    public void onShowProgress() {
       /* if (progressDialog!=null)
            progressDialog.show();*/
       mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onHideProgress() {
       /* if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();*/
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
    public void onShopDataReceivedSuccess(List<ShopListResponseModel.DataBean.HitsBean> shopListData) {
        if (shopListData.size()>0){
            shopList = new ArrayList<>();
            shopList.addAll(shopListData);
            shopListAdapter = new ShopListAdapter(mContext,shopList);
            shopListAdapter.setListener(this);
            if (shopListRecyclerView!=null)
            shopListRecyclerView.setAdapter(shopListAdapter);
        }
    }

    @Override
    public void onLoadBannerListSuccess(List<String> bannerListData) {
        bannerAdapter = new BannerAdapter(mContext,bannerListData);
        bannerRecyclerView.setAdapter(bannerAdapter);
    }

    @Override
    public void onLoadCategoriesSuccess(List<CategoryResponseModel.DataBean> categoryList) {
        categoryHomeAdapter = new CategoryHomeAdapter(mContext,categoryList);
        categoryHomeAdapter.setListener(this);
        if (categoryRecyclerView!=null)
        categoryRecyclerView.setAdapter(categoryHomeAdapter);
    }

    @Override
    public void onCallButtonClicked(String shopId, String shopName, String primaryImage) {
        if (AppUtils.isUserLoggedIn(mContext)){
            if (NetworkUtil.isConnected(mContext)){
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



    @Override
    public void onSelectCategory(CategoryResponseModel.DataBean category) {
        Intent searchIntent = new Intent(mContext, SearchResultActivity.class);
        searchIntent.putExtra(Constants.KEY_CATEGORY_ID,category.getId());
        searchIntent.putExtra(Constants.KEY_CATEGORY_NAME,category.getName());
        searchIntent.putExtra(Constants.KEY_LATITUDE,latitude);
        searchIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
        startActivity(searchIntent);
    }
}
