package com.weedeo.user.ui.shoplisting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.weedeo.user.R;
import com.weedeo.user.Utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Athul on 07-11-2019.
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> bannerList;

    public BannerAdapter(Context mContext, List<String> bannerList) {
        this.mContext = mContext;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_list_coloumn, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("banner","image");
        Glide.with(mContext)
                .load(R.drawable.sample_banner)
                .placeholder(R.drawable.sample_image)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(holder.banImageView);
        //holder.banImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sample_image));
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bannerImageView)
        ImageView banImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //EventBus.getDefault().post(new MessageEvent("name","514654654","https://www.gstatic.com/webp/gallery/4.webp"));
                }
            });
        }
    }
}
