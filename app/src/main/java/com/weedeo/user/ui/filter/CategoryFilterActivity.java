package com.weedeo.user.ui.filter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.weedeo.user.R;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.model.CategoryResponseModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class CategoryFilterActivity extends BaseActivity implements CategoryFilterContract.MvpView{

    private CategoryFilterPresenter categoryFilterPresenter;
    private Context mContext;
    private CategoryAdapter adapter;



    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CategoryFilterActivity.this;
        categoryListView.setLayoutManager(new LinearLayoutManager(this));
        categoryListView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        categoryFilterPresenter = new CategoryFilterPresenter(this,mContext);
        categoryFilterPresenter.onGetCategoryList();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_category_filter;
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

    }

    @Override
    public void onHideProgress() {

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

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onCategoryListRetrievedSuccessFully(CategoryResponseModel categoryResponseModel) {
        List<CategoryResponseModel.DataBean> categoryList = categoryResponseModel.getData();
        if (categoryList.size()>0){
            Toast.makeText(mContext, String.valueOf(categoryList.size()), Toast.LENGTH_SHORT).show();
            adapter = new CategoryAdapter(mContext,categoryList);
            categoryListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
}
