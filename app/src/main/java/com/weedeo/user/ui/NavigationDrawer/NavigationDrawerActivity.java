package com.weedeo.user.ui.NavigationDrawer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hypertrack.hyperlog.HyperLog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.weedeo.user.R;
import com.weedeo.user.Utils.AppUtils;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.Utils.MessageEvent;
import com.weedeo.user.Utils.NetworkUtil;
import com.weedeo.user.base.BaseActivity;
import com.weedeo.user.sharedpreference.SharedPreferenceData;
import com.weedeo.user.ui.changenumber.ChangeNumberFragment;
import com.weedeo.user.ui.filter.CategoryFilterActivity;
import com.weedeo.user.ui.filter.CategoryFilterFragment;
import com.weedeo.user.ui.login.LoginActivity;
import com.weedeo.user.ui.map.FetchAddressIntentService;
import com.weedeo.user.ui.map.MapActivity;
import com.weedeo.user.ui.orderhistory.OrderHistoryFragment;
import com.weedeo.user.ui.returnproduct.ReturnProductFragment;
import com.weedeo.user.ui.shoplisting.ShopListingFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class NavigationDrawerActivity extends BaseActivity implements NavigationContract.MvpView,NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener {

    private String TAG = "SplashActivity";
    private NavigationPresenter navigationPresenter;
    private Context mContext;
    private Dialog progressDialog;
    private HeaderViewHolder mHeaderViewHolder;
    private boolean isActivityPaused = false;
    private double latitude = 0, longitude = 0;
    private static int MAP_RESPONSE_CODE = 121;
    private MenuItem profileMenu;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private NavigationDrawerActivity.AddressResultReceiver mResultReceiver;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.locationView)
    LinearLayout locationView;

    @BindView(R.id.locationTextView)
    TextView locationTextView;

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyperLog.i(TAG,"Activity Started");
        Toolbar toolbar = findViewById(R.id.navigationToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_home));
        }
        this.mContext = NavigationDrawerActivity.this;
        progressDialog = AppUtils.showProgressDialog(mContext);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationPresenter = new NavigationPresenter(this,NavigationDrawerActivity.this);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mHeaderViewHolder = new HeaderViewHolder(header);
        if (getIntent().getExtras()!=null){ mResultReceiver = new NavigationDrawerActivity.AddressResultReceiver(new Handler());
            latitude = getIntent().getDoubleExtra(Constants.KEY_LATITUDE,0);
            longitude = getIntent().getDoubleExtra(Constants.KEY_LONGITUDE,0);
            if (latitude!=0 && longitude!=0){
                try {
                    Log.e("receive location lat : ", String.valueOf(latitude));
                    Log.e("receive location lng : ", String.valueOf(longitude));
                    Location mLocation = new Location("");
                    mLocation.setLatitude(latitude);
                    mLocation.setLongitude(longitude);
                    startIntentService(mLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else
                navigationPresenter.getCurrentLocation();
        }else {
            navigationPresenter.getCurrentLocation();
        }
        navigationPresenter.checkUserLoginStatus();
        if (savedInstanceState == null) {
            openShopListingFragment();
            navigationView.setCheckedItem(R.id.nav_shop_listing);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
        bottomNavigationView.getMenu().getItem(3).setCheckable(false);
    }


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(com.weedeo.user.ui.map.AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(com.weedeo.user.ui.map.AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
           /* mAddressOutput = resultData.getString(com.weedeo.user.ui.map.AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(com.weedeo.user.ui.map.AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(com.weedeo.user.ui.map.AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(com.weedeo.user.ui.map.AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();*/

           locationTextView.setText(resultData.getString(com.weedeo.user.ui.map.AppUtils.LocationConstants.LOCATION_DATA_STREET));

            // Show a toast message if an address was found.
            if (resultCode == com.weedeo.user.ui.map.AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search_home));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NetworkUtil.isConnected(mContext)){
            switch (item.getItemId()){
                case R.id.search_menu:
                    openSearchFragment();
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
            }
        }else
            onShowToast(getString(R.string.no_internet));

        return super.onOptionsItemSelected(item);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Glide.with(this)
                .load(event.imageUrl)
                .placeholder(R.drawable.default_user)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mHeaderViewHolder.userImage);
        Log.e("onMessageEvent","onMessageEvent");
        Toast.makeText(mContext, event.name+" , "+event.imageUrl, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfileImage(String imageUrl) {
        HyperLog.i(TAG,"setProfileImage-Executed");
    }

    @Override
    public void guestUser() {
       mHeaderViewHolder.userName.setText(getString(R.string.guest_user_name));
       mHeaderViewHolder.userMobile.setVisibility(View.GONE);
    }

    @Override
    public void loggedInUser() {
        String image = Constants.PROFILE_IMAGE_EXTENTION + navigationPresenter.getProfileImageFromDB();
        Log.e("image path drawer : ",image);

        mHeaderViewHolder.userImage.setImageURI(Uri.parse(image));
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();


        /*Glide.with(this)
                .load(image)
                .placeholder(R.drawable.default_user)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mHeaderViewHolder.userImage);*/
        if (navigationPresenter.getUserNameFromDB()!=null&&navigationPresenter.getUserNameFromDB().length()>0)
            mHeaderViewHolder.userName.setText(navigationPresenter.getUserNameFromDB());
        else
                mHeaderViewHolder.userName.setText(getString(R.string.guest_user_name));
        mHeaderViewHolder.userMobile.setText(navigationPresenter.getUserNumberFromDB());
    }

    @Override
    public void onCurrentLocationReceivedSuccessfully(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (latitude!=0 && longitude!=0){
            try {
                Log.e("receive location lat : ", String.valueOf(latitude));
                Log.e("receive location lng : ", String.valueOf(longitude));
                Location mLocation = new Location("");
                mLocation.setLatitude(latitude);
                mLocation.setLongitude(longitude);
                startIntentService(mLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_shop_listing:
            case R.id.home:
                openShopListingFragment();
                break;
            case R.id.search:
                openSearchFragment();
                break;
            case R.id.nav_change_number:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChangeNumberFragment()).commit();*/
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out the app at: https://play.google.com/store/apps/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container,
                        new OrderHistoryFragment(),Constants.CART_FRAGMENT_TAG).commit();
                if (getSupportActionBar()!=null)
                    getSupportActionBar().setTitle(getString(R.string.cart_text));
                locationView.setVisibility(View.GONE);
                break;
            case R.id.profile:
                if (AppUtils.isUserLoggedIn(mContext))
                    navigationPresenter.startProfileActivity();
                else{
                    setPreviouslyVisibleFragmentMenuSelection();
                    onShowToast("You are not logged in");
                }

                /*getSupportFragmentManager().beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).replace(R.id.fragment_container,
                        new ReturnProductFragment()).commit();*/
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSearchFragment() {
        CategoryFilterFragment categoryFilterFragment = new CategoryFilterFragment();
        Bundle args = new Bundle();
        args.putDouble(Constants.KEY_LATITUDE,latitude);
        args.putDouble(Constants.KEY_LONGITUDE,longitude);
        categoryFilterFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container,
                categoryFilterFragment,Constants.SEARCH_FRAGMENT_TAG).commit();
        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle(getString(R.string.search_text));
        locationView.setVisibility(View.GONE);
    }

    private void openShopListingFragment() {
            locationView.setVisibility(View.VISIBLE);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            ShopListingFragment shopListingFragment = new ShopListingFragment();
            Bundle args = new Bundle();
            args.putDouble(Constants.KEY_LATITUDE,latitude);
            args.putDouble(Constants.KEY_LONGITUDE,longitude);
            shopListingFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN ).replace(R.id.fragment_container,
                    shopListingFragment,Constants.HOME_FRAGMENT_TAG).commit();

    }


    @OnClick(R.id.locationView)
    public void onLocationViewClicked(){
        Log.e("onLocationViewClicked","onLocationViewClicked");
        if (NetworkUtil.isConnected(this)) {
            Intent mapIntent = new Intent(NavigationDrawerActivity.this, MapActivity.class);
            mapIntent.putExtra(Constants.KEY_CLASS_REFERENCE,"NavigationDrawerActivity");
            startActivityForResult(mapIntent,MAP_RESPONSE_CODE);
        }else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_RESPONSE_CODE && resultCode == Activity.RESULT_OK) {
            latitude = data.getDoubleExtra(Constants.KEY_LATITUDE,0);
            longitude = data.getDoubleExtra(Constants.KEY_LONGITUDE,0);
            locationTextView.setText(data.getStringExtra(Constants.KEY_CITY));
            openShopListingFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (AppUtils.isUserLoggedIn(mContext)){
                ShopListingFragment homeFragment = (ShopListingFragment) getSupportFragmentManager().findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
                if (homeFragment!=null && homeFragment.isVisible()){
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.exit_message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes_text, (dialog, id) -> finishAffinity())
                            .setNegativeButton(R.string.no_text, null)
                            .show();
                }else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    openShopListingFragment();
                }

            } else {
                Intent loginIntent = new Intent(NavigationDrawerActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finishAffinity();
            }
        }
    }

    @Override
    public void onShowProgress() {
        if (progressDialog!=null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String message) {

    }

    @Override
    public void onCheckInternetConnection() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRetry() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setPreviouslyVisibleFragmentMenuSelection();
        SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(mContext);
        //Updating user fields in drawer
        updateUserDate(sharedPreferenceData.getBool(Constants.KEY_IS_PROFILE_UPDATE));
        sharedPreferenceData.setBool(Constants.KEY_IS_PROFILE_UPDATE,false);

    }

    private void updateUserDate(boolean isUpdate) {
        if (isUpdate){
            String image = Constants.PROFILE_IMAGE_EXTENTION + navigationPresenter.getProfileImageFromDB();
            Log.e("image path drawer : ",image);

            mHeaderViewHolder.userImage.setImageURI(Uri.parse(image));
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.clearCaches();

            /*Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.default_user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(mHeaderViewHolder.userImage);*/
        }
        if (navigationPresenter.getUserNameFromDB()!=null)
            mHeaderViewHolder.userName.setText(navigationPresenter.getUserNameFromDB());
        else
            mHeaderViewHolder.userName.setText(getString(R.string.guest_user_name));
        mHeaderViewHolder.userMobile.setText(navigationPresenter.getUserNumberFromDB());
    }

    private void setPreviouslyVisibleFragmentMenuSelection() {
        ShopListingFragment homeFragment = (ShopListingFragment) getSupportFragmentManager().findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
        CategoryFilterFragment filterFragment = (CategoryFilterFragment) getSupportFragmentManager().findFragmentByTag(Constants.SEARCH_FRAGMENT_TAG);
        OrderHistoryFragment cartFragment = (OrderHistoryFragment) getSupportFragmentManager().findFragmentByTag(Constants.CART_FRAGMENT_TAG);
        if (homeFragment != null && homeFragment.isVisible())
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        else if (filterFragment != null && filterFragment.isVisible())
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        else if (cartFragment != null && cartFragment.isVisible())
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }


    class HeaderViewHolder {

         @BindView(R.id.user_drawer_image)
         SimpleDraweeView userImage;

         @BindView(R.id.userNameTextView)
         TextView userName;

         @BindView(R.id.userMobileTextView)
         TextView userMobile;

         @BindView(R.id.navigationHeader)
         RelativeLayout navigationHeader;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.navigationHeader)
        public void onProfileClick() {
            if (AppUtils.isUserLoggedIn(mContext))
                navigationPresenter.startProfileActivity();
            else
                onShowToast("You are not logged in");
        }
    }


}