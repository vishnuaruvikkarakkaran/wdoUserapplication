package com.weedeo.user.ui.address.addnew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weedeo.user.R;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.CityResponseModel;
import com.weedeo.user.model.CitySearchResponse;
import com.weedeo.user.model.request.CitySearchRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySelectionDialog extends Dialog implements CitySelectionAdapter.CityAdapterListener {

    private Context mContext;
    private CitySelectionAdapter citySelectionAdapter;
    private CitySelectionDialogListener listener;
    private List<CityResponseModel.DataBean> cityList;
    private String stateName;

    @BindView(R.id.dialogTitle)
    TextView dialogTitle;

    @BindView(R.id.searchCities)
    EditText searchCities;

    @BindView(R.id.city_list_view)
    RecyclerView cityListView;

    public CitySelectionDialog(@NonNull Context context, String state) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.city_selection_dialog);
        this.stateName = state;
        ButterKnife.bind(this, getWindow().getDecorView());
        dialogTitle.setText(mContext.getString(R.string.select_city_text));
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        // listening to search query text change
        searchCities.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // filter recycler view when text is changed
                CharSequence charSequence = editable.toString();
                citySelectionAdapter.getFilter().filter(charSequence);
            }
        });


        cityList = new ArrayList<>();
        citySelectionAdapter = new CitySelectionAdapter(mContext,cityList,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        cityListView.setLayoutManager(mLayoutManager);
        cityListView.setItemAnimator(new DefaultItemAnimator());
        cityListView.addItemDecoration(new ItemDecorator(mContext));
        cityListView.setAdapter(citySelectionAdapter);


        fetchCities(stateName);
        
    }

    /**
     * fetches json by making http calls
     * @param stateName
     */
    private void fetchCities(String stateName) {
        WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        CitySearchRequest citySearchRequest = new CitySearchRequest();
        citySearchRequest.setState(stateName);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(citySearchRequest));
        Call<String> call =  service.getCityDetails(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    if (response.code()== Constants.SUCCESS_CODE){
                        if (response.body()!=null){
                            Log.e("Request : ",new Gson().toJson(citySearchRequest));
                            Log.e("response : ",response.body());
                            Type listType = new TypeToken<CityResponseModel>() {
                            }.getType();
                            CityResponseModel cityResponseModel = new GsonBuilder().create().fromJson(response.body(), listType);
                            if (cityResponseModel.getStatus().equals(Constants.SUCCESS)){
                                if (cityResponseModel.getData()!=null){
                                    // adding contacts to contacts list
                                    cityList.clear();
                                    cityList.addAll(cityResponseModel.getData());

                                    // refreshing recycler view
                                    citySelectionAdapter.notifyDataSetChanged();
                                }

                            }

                        }

                    }else if (response.code()==Constants.ERROR_CODE){
                    }else if (response.code()==Constants.ERROR_BAD_GATEWAY_CODE){
                    }else if (response.code()==Constants.ERROR_UNAUTHORIZED_CODE){

                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //HyperLog.i(TAG,"onUserLogin Fails - "+t.getMessage());
                Log.e("Failure Message : ",t.getMessage());
            }

        });

    }

    @OnClick(R.id.closeCityDialog)
    void close(){
        dismiss();
    }

    @Override
    public void onCitySelected(CityResponseModel.DataBean cityModel) {
        listener.onCitySelected(cityModel);
        dismiss();
    }

    public void setListener(CitySelectionDialogListener citySelectionDialogListener){
        this.listener = citySelectionDialogListener;
    }


    public interface CitySelectionDialogListener  {
        void onCitySelected(CityResponseModel.DataBean cityModel);
    }
}
