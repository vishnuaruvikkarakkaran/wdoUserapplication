package com.weedeo.user.ui.address;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;
import com.weedeo.user.Utils.Constants;
import com.weedeo.user.data.ApiClient;
import com.weedeo.user.data.WebApiListener;
import com.weedeo.user.model.CitySearchResponse;
import com.weedeo.user.model.request.CitySearchRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteAdapter extends ArrayAdapter<CitySearchResponse.Cities> implements Filterable {
    private List<CitySearchResponse.Cities> mData;

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CitySearchResponse.Cities getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                /*if(constraint != null) {

                    WebApiListener service = ApiClient.getRetrofitInstance().create(WebApiListener.class);
                    CitySearchRequest citySearchRequest = new CitySearchRequest();
                    citySearchRequest.setSearch_key(constraint.toString());

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(citySearchRequest));
                    Call<String> call =  service.getCityDetails(requestBody);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                if (response.code()== Constants.SUCCESS_CODE);{
                                    if (response.body()!=null){
                                        Log.e("Login Request : ",new Gson().toJson(citySearchRequest));
                                        Log.e("response : ",response.body());
                                        Type listType = new TypeToken<CitySearchResponse>() {
                                        }.getType();
                                        CitySearchResponse citySearchResponse = new GsonBuilder().create().fromJson(response.body(), listType);
                                        if (citySearchResponse.getStatus().equals(Constants.SUCCESS)){
                                            if (citySearchResponse.getCity()!=null){
                                                mData = citySearchResponse.getCity();
                                            }

                                        }
                                    }

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

                    filterResults.values = mData;
                    filterResults.count = mData.size();

                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                    *//*StyleFetcher fetcher = new StyleFetcher();
                    try {
                        mData = fetcher.retrieveResults(constraint.toString());
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();*//*
                }*/
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}