package com.weedeo.user.ui.shoplisting;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.model.CategoryResponseModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Athul on 17-11-2019.
 */
public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.MyViewHolder>  {

    private Context mContext;
    private List<CategoryResponseModel.DataBean> categoryList;
    private AdapterListener listener;

    public CategoryHomeAdapter(Context mContext, List<CategoryResponseModel.DataBean> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_horizontal_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryResponseModel.DataBean category = categoryList.get(position);
        Log.e("category image : ",Constants.CATEGORY_IMAGE_EXTENTION+category.getImageUrl());
        holder.categoryImage.setImageURI(Uri.parse(Constants.CATEGORY_IMAGE_EXTENTION+category.getImageUrl()));
        /*Glide.with(mContext)
                .load(Constants.SHOP_IMAGE_EXTENTION+category.getImageUrl())
                .placeholder(R.drawable.ic_circular_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.categoryImage);
*/
        holder.categoryName.setText(category.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectCategory(categoryList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryImage)
        SimpleDraweeView categoryImage;

        @BindView(R.id.categoryName)
        TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }

    public void setListener(AdapterListener adapterListener){
        this.listener = adapterListener;
    }

    public interface AdapterListener  {
        void onSelectCategory(CategoryResponseModel.DataBean category);
    }
}
