package com.weedeo.user.ui.shoplisting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.databse.DatabaseContract;
import com.weedeo.user.databse.DatabasePresenter;
import com.weedeo.user.model.ShopListResponseModel;
import com.weedeo.user.ui.address.edit.AddressListAdapter;
import com.weedeo.user.ui.call.VideoCallActivity;

import org.apache.commons.text.WordUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.weedeo.user.Utils.Constants.KEY_USER_IMAGE;
import static com.weedeo.user.Utils.Constants.KEY_USER_NAME;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ShopListResponseModel.DataBean.HitsBean> shopList;
    private static final int VIEW_TYPE_DATA = 1;
    private static final int VIEW_TYPE_EMPTY = 0;
    private ShopListAdapterListener listener;


    public ShopListAdapter(Context mContext, List<ShopListResponseModel.DataBean.HitsBean> shopList) {
        this.mContext = mContext;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType==VIEW_TYPE_DATA)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shop_list_row, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_view, parent, false);

        return new ShopListAdapter.MyViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType==VIEW_TYPE_DATA){
            ShopListResponseModel.DataBean.HitsBean shopData = shopList.get(position);
            if (shopData.get_source().getPrimary_image_status()==2){
                Glide.with(mContext)
                        .load(Constants.SHOP_IMAGE_EXTENTION+shopData.get_id()+"/"+shopList.get(position).get_source().getPrimary_image())
                        .placeholder(R.drawable.shop_list_placeholder)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                        .into(holder.shopImageView);
            }else {
                Glide.with(mContext)
                        .load(R.drawable.shop_list_placeholder)
                        .placeholder(R.drawable.shop_list_placeholder)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                        .into(holder.shopImageView);
            }
            holder.shopName.setText(WordUtils.capitalize(shopData.get_source().getShop_name()));
            holder.shopAddress.setText(WordUtils.capitalize(shopData.get_source().getCity().getName()));
            holder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        if (shopList.get(position).get_source().getPrimary_image_status()==2)
                            listener.onCallButtonClicked(shopList.get(position).get_id(),shopList.get(position).get_source().getShop_name(),shopList.get(position).get_source().getPrimary_image());
                        else
                            listener.onCallButtonClicked(shopList.get(position).get_id(),shopList.get(position).get_source().getShop_name(),null);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(shopList.size() == 0){
            return 1;
        }else {
            return shopList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (shopList.size()==0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_DATA;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.listShopName)
        TextView shopName;

        @BindView(R.id.listShopAddress)
        TextView shopAddress;

        @BindView(R.id.listRoundedImageView)
        ImageView shopImageView;

        @BindView(R.id.call_button)
        LinearLayout callButton;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType==VIEW_TYPE_DATA)
            ButterKnife.bind(this,itemView);
        }
    }

    public void setListener(ShopListAdapterListener adapterListener){
        this.listener = adapterListener;
    }

    public interface ShopListAdapterListener{
        void onCallButtonClicked(String shopId, String shopName, String primaryImage);
    }
}
