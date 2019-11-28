package com.weedeo.user.ui.shoplisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.weedeo.user.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Athul on 08-11-2019.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> bannerList;
    private LayoutInflater inflater;

    @BindView(R.id.bannerImageView)
    ImageView bannerImageView;

    public BannerPagerAdapter(Context context, List<String> bannerList) {
        this.mContext = context;
        this.bannerList = bannerList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.banner_list_coloumn, container, false);
        ButterKnife.bind(this,itemView);

        bannerImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sample_image));

        container.addView(itemView);

        //listening to image click
        bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
