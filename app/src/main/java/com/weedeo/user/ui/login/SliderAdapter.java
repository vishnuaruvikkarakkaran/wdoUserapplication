package com.weedeo.user.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.weedeo.user.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:
                viewHolder.imageViewBackground.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider1));
                viewHolder.textViewDescription.setText(context.getString(R.string.slider_2_title));
                break;
            case 1:
                viewHolder.imageViewBackground.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider2));
                viewHolder.textViewDescription.setText(context.getString(R.string.slider_2_title));
                break;
            case 2:
                viewHolder.imageViewBackground.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider3));
                viewHolder.textViewDescription.setText(context.getString(R.string.slider_1_title));
                break;
        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 3;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
