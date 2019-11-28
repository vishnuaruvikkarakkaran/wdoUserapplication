package com.weedeo.user.ui.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputLayout;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaceActivity extends BaseActivity implements PlaceAutocompleteAdapter.ClickListener {

    private PlaceAutocompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private String TAG = "PlaceActivity";

    @BindView(R.id.placeToolbar)
    Toolbar toolbar;

    @BindView(R.id.currentLocationLayout)
    LinearLayout currentLocationLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyperLog.i(TAG,"Activity Started");
        Places.initialize(this, getResources().getString(R.string.google_maps_key));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.places_recycler_view);
        ((EditText) findViewById(R.id.place_search)).addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlaceAutocompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();



    }

    @OnClick(R.id.currentLocationLayout)
    public void onClick(){
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_SET_CURRENT_LOCATION,"location");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_place;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length()>0)
                currentLocationLayout.setVisibility(View.GONE);
            else
                currentLocationLayout.setVisibility(View.VISIBLE);

        }
    };

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
    public void click(Place place) {
        Intent intent = new Intent();
        intent.putExtra(Constants.PLACE, place);
        setResult(RESULT_OK, intent);
        finish();
    }


}
