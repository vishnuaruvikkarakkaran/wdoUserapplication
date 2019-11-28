package com.weedeo.user.ui.filter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.model.CategoryResponseModel;
import com.weedeo.user.ui.searchresult.SearchResultActivity;

import java.util.List;

/**
 * Created By Athul on 14-11-2019.
 */
public class CategoryAdapter extends ExpandableRecyclerAdapter<CategoryViewHolder, SubCategoryViewHolder> {

    private LayoutInflater mInflator;
    private Context mContext;
    private CategoryAdapterListener listener;

    public CategoryAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflator.inflate(R.layout.category_view_item, parentViewGroup, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public SubCategoryViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflator.inflate(R.layout.subcategory_view_item, childViewGroup, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(CategoryViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        parentViewHolder.setIsRecyclable(false);
        CategoryResponseModel.DataBean category = (CategoryResponseModel.DataBean)parentListItem;
        parentViewHolder.bind(category);

        if (((CategoryResponseModel.DataBean) parentListItem).getSubCategories().size()==0){
            parentViewHolder.categoryTextView.setOnClickListener(view -> {
                listener.onCategoryOptionSelected(category);
            });
        }

    }

    @Override
    public void onBindChildViewHolder(SubCategoryViewHolder childViewHolder, int position, Object childListItem) {
       // childViewHolder.setIsRecyclable(false);
        CategoryResponseModel.DataBean.SubCategoriesBean subCategory = (CategoryResponseModel.DataBean.SubCategoriesBean)childListItem;
        childViewHolder.bind(subCategory);
        childViewHolder.itemView.setOnClickListener(view -> {
            listener.onSubCategoryOptionSelected(subCategory);
        });
    }

    public void setListener(CategoryAdapterListener adapterListener){
        this.listener = adapterListener;
    }

    public interface CategoryAdapterListener{
        void onCategoryOptionSelected(CategoryResponseModel.DataBean categoryData);
        void onSubCategoryOptionSelected(CategoryResponseModel.DataBean.SubCategoriesBean subCategoryData);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    /*@Override
    public int getItemViewType(int position) {
        return position;
    }*/

}
