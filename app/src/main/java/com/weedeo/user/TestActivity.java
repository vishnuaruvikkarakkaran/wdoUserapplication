package com.weedeo.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.weedeo.user.Utils.Constants;

public class TestActivity extends AppCompatActivity {


    String path = "https://weedeo-dev-ap-south-1.s3.ap-south-1.amazonaws.com/shop/5dca5a4b6c729f0008ad887b/shop_profile/IMG_20191121_105809101784741.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ImageView imageView = findViewById(R.id.imageView);

        Glide.with(getApplicationContext())
                .load(path)
                .placeholder(R.drawable.shop_list_placeholder)
                .into(imageView);
    }
}
