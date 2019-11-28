package com.weedeo.user.ui.address.addnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.weedeo.user.R;
import com.weedeo.user.model.CityResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Athul on 21-10-2019.
 */
public class CitySelectionAdapter extends RecyclerView.Adapter<CitySelectionAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<CityResponseModel.DataBean> cityList;
    private List<CityResponseModel.DataBean> filteredCityList;
    private CityAdapterListener listener;
    private static final int VIEW_TYPE_DATA = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.cityName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected city in callback
                    listener.onCitySelected(filteredCityList.get(getAdapterPosition()));
                }
            });
        }
    }


    public CitySelectionAdapter(Context context, List<CityResponseModel.DataBean> list, CityAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cityList = list;
        this.filteredCityList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType==VIEW_TYPE_DATA)
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_custom_list_view, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType==VIEW_TYPE_DATA)
            holder.name.setText(filteredCityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(filteredCityList.size() == 0){
            return 1;
        }else {
            return filteredCityList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (filteredCityList.size()==0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_DATA;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredCityList = cityList;
                } else {
                    List<CityResponseModel.DataBean> filteredList = new ArrayList<>();
                    for (CityResponseModel.DataBean row : cityList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filteredCityList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCityList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCityList = (ArrayList<CityResponseModel.DataBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CityAdapterListener {
        void onCitySelected(CityResponseModel.DataBean cityModel);
    }
}
