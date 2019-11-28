package com.weedeo.user.ui.filter;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.Utils.NoInternetDialog;
import com.weedeo.user.base.BaseFragment;
import com.weedeo.user.model.CategoryResponseModel;
import com.weedeo.user.ui.NavigationDrawer.NavigationDrawerActivity;
import com.weedeo.user.ui.searchresult.SearchResultActivity;

import java.util.List;

import butterknife.BindView;

public class CategoryFilterFragment extends BaseFragment implements CategoryFilterContract.MvpView, CategoryAdapter.CategoryAdapterListener,
                    NoInternetDialog.RetryListener{

    private CategoryFilterPresenter categoryFilterPresenter;
    private Context mContext;
    private CategoryAdapter adapter;
    private boolean isFilterViewUp;
    private double latitude = 0;
    private double longitude = 0;

    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryListView;

    @BindView(R.id.filterLayout)
    LinearLayout filterLayout;

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        onCheckInternetConnection();
    }

    private void init(){
        Bundle args = getArguments();
        if (args!=null){
            latitude = args.getDouble(Constants.KEY_LATITUDE);
            longitude = args.getDouble(Constants.KEY_LONGITUDE);
        }

        Log.e("cat latitude : ", String.valueOf(latitude));
        Log.e("cat longitude : ", String.valueOf(longitude));
        isFilterViewUp = true;
        categoryListView.setLayoutManager(new LinearLayoutManager(mContext));
        categoryFilterPresenter = new CategoryFilterPresenter(this,mContext);
        categoryFilterPresenter.onGetCategoryList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_category_filter;
    }

    @Override
    public void onCategoryListRetrievedSuccessFully(CategoryResponseModel categoryResponseModel) {
        List<CategoryResponseModel.DataBean> categoryList = categoryResponseModel.getData();
        if (categoryList.size()>0){
            adapter = new CategoryAdapter(mContext,categoryList);
            adapter.setListener(this);
            if (categoryListView!=null)
                categoryListView.setAdapter(adapter);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem actionSearchItem =  menu.findItem(R.id.action_search);
        if (actionSearchItem!=null)
            actionSearchItem.setVisible(true);
        MenuItem filterItem = menu.findItem(R.id.filter_menu);
        if (filterItem!=null)
            filterItem.setVisible(true);
        MenuItem searchMenu = menu.findItem(R.id.search_menu);
        if (searchMenu!=null)
            searchMenu.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NetworkUtil.isConnected(mContext)){
            switch (item.getItemId()){
                case R.id.filter_menu:

                    if (isFilterViewUp) {
                        AppUtils.collapse(filterLayout);
                    } else {
                        AppUtils.expand(filterLayout);
                    }
                    isFilterViewUp = !isFilterViewUp;
                    break;
            }
        }else
            onShowToast(getString(R.string.no_internet));

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView = searchView.findViewById(id);
        textView.setTextSize(16);
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.opensans_regular);
        textView.setTypeface(typeface);
        textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
        searchView.setQueryHint(getString(R.string.action_search_hint));
        searchView.setFocusable(true);
        searchView.setIconified(true);
        searchView.requestFocusFromTouch();
        //searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(mContext, SearchResultActivity.class);
                searchIntent.putExtra(Constants.KEY_CATEGORY_NAME,query);
                searchIntent.putExtra(Constants.KEY_LATITUDE,latitude);
                searchIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
                startActivity(searchIntent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (filterLayout!=null){
                        isFilterViewUp = false;
                        AppUtils.collapse(filterLayout);
                        MenuItem filterItem = menu.findItem(R.id.filter_menu);
                        if (filterItem!=null)
                            filterItem.setVisible(true);
                    }
                }else {
                    if (filterLayout!=null){
                        isFilterViewUp = true;
                        AppUtils.expand(filterLayout);
                    }

                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onCategoryOptionSelected(CategoryResponseModel.DataBean categoryData) {
        Intent searchIntent = new Intent(mContext, SearchResultActivity.class);
        searchIntent.putExtra(Constants.KEY_CATEGORY_ID,categoryData.getId());
        searchIntent.putExtra(Constants.KEY_CATEGORY_NAME,categoryData.getName());
        searchIntent.putExtra(Constants.KEY_LATITUDE,latitude);
        searchIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
        startActivity(searchIntent);
    }

    @Override
    public void onSubCategoryOptionSelected(CategoryResponseModel.DataBean.SubCategoriesBean subCategoryData) {
        Intent searchIntent = new Intent(mContext, SearchResultActivity.class);
        searchIntent.putExtra(Constants.KEY_CATEGORY_ID,subCategoryData.getId());
        searchIntent.putExtra(Constants.KEY_CATEGORY_NAME,subCategoryData.getName());
        searchIntent.putExtra(Constants.KEY_LATITUDE,latitude);
        searchIntent.putExtra(Constants.KEY_LONGITUDE,longitude);
        startActivity(searchIntent);
    }
}
