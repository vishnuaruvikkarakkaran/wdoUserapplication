package com.weedeo.user.ui.address.edit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weedeo.user.R;
import com.weedeo.user.model.AddressFullListResponseModel;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    private Context mContext;
    private List<AddressFullListResponseModel.DataBean> addressList;
    private AdapterListener listener;
    private static final int VIEW_TYPE_DATA = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

    public AddressListAdapter(Context mContext, List<AddressFullListResponseModel.DataBean> list) {
        this.mContext = mContext;
        this.addressList = list;
    }

    @NonNull
    @Override
    public AddressListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType==VIEW_TYPE_DATA)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.address_list_row, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.MyViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType==VIEW_TYPE_DATA){
            AddressFullListResponseModel.DataBean addressData = addressList.get(position);
            holder.name.setText(addressData.getName());

            StringBuilder builder = new StringBuilder();
            if (addressData.getAddress_line1()!=null)
                builder.append(addressData.getAddress_line1()).append(",");

            if (addressData.getAddress_line2()!=null)
                builder.append(addressData.getAddress_line2()).append(", \n");

            if (!addressData.getLand_mark().equals(""))
                builder.append(addressData.getLand_mark()).append(", \n");

            if (addressData.getCity()!=null)
                builder.append(addressData.getCity().getName()).append(", ");

            if (addressData.getState()!=null)
                builder.append(addressData.getState()).append("-");

            builder.append(addressData.getPincode()).append("\n");

            builder.append(addressData.getMobile_number());

            if (!addressData.getSecondary_number().equals(""))
                builder.append(", \n").append(addressData.getSecondary_number());

            holder.address.setText(builder);

            if (addressData.isPrimary_status()){
                holder.setDefault.setChecked(true);
                holder.deleteAddress.setVisibility(View.INVISIBLE);
            }else {
                holder.setDefault.setChecked(false);
                holder.deleteAddress.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(view -> {
                if (listener!=null)
                    listener.onItemClicked(addressData);
            });

            holder.setDefault.setOnClickListener(view -> {
                if (listener!=null)
                    listener.onSetPrimaryAddress(addressData.getUser_id(), addressData.getId());
            });

            holder.deleteAddress.setOnClickListener(view -> {
                if (listener!=null)
                    listener.onShowDeleteAddressAlertDialog(addressData.getId());
            });
        }
    }

    @Override
    public int getItemCount() {
        if(addressList.size() == 0){
            return 1;
        }else {
            return addressList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (addressList.size()==0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_DATA;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;
        public RadioButton setDefault;
        public ImageView deleteAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.userName);
            address = itemView.findViewById(R.id.address);
            setDefault = itemView.findViewById(R.id.setDefault);
            deleteAddress = itemView.findViewById(R.id.deleteAddress);
        }
    }


    public void setListener(AdapterListener adapterListener){
        this.listener = adapterListener;
    }

    public interface AdapterListener  {
        void onItemClicked(AddressFullListResponseModel.DataBean dataBean);
        void onSetPrimaryAddress(String userId, String addressId);
        void onShowDeleteAddressAlertDialog(String addressId);
    }

}
