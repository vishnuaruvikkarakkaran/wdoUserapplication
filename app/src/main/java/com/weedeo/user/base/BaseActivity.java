package com.weedeo.user.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
    }

    /**
     * get layout to inflate
     */
    public abstract
    @LayoutRes
    int getLayout();
}
