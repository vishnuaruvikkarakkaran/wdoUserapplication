package com.weedeo.user.ui.filter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.weedeo.user.R;
import com.weedeo.user.model.CategoryResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Athul on 14-11-2019.
 */
public class SubCategoryViewHolder extends ChildViewHolder {

    //@BindView(R.id.sub_text_view)
    TextView subCategoryTextView;

    public SubCategoryViewHolder(View itemView) {
        super(itemView);
       // ButterKnife.bind(this,itemView);
        subCategoryTextView = itemView.findViewById(R.id.sub_text_view);
    }

    public void bind(CategoryResponseModel.DataBean.SubCategoriesBean subCategory) {
        Log.e("name : ",subCategory.getName());
        if (subCategory.getName()==null)
            subCategoryTextView.setText("null");
        else
            subCategoryTextView.setText(subCategory.getName());
    }
}
