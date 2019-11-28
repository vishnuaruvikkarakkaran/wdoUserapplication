package com.weedeo.user.ui.address.addnew;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weedeo.user.R;
import com.weedeo.user.model.CityResponseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StateSelectionDialog extends Dialog implements CitySelectionAdapter.CityAdapterListener {

    private Context mContext;
    private CitySelectionAdapter stateSelectionAdapter;
    private StateSelectionDialogListener listener;

    @BindView(R.id.dialogTitle)
    TextView dialogTitle;

    @BindView(R.id.searchCities)
    EditText searchStates;

    @BindView(R.id.city_list_view)
    RecyclerView stateListView;

    public StateSelectionDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.city_selection_dialog);
        ButterKnife.bind(this, getWindow().getDecorView());
        dialogTitle.setText(mContext.getString(R.string.select_state_text));
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // listening to search query text change
        searchStates.addTextChangedListener(new TextWatcher() {
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
                stateSelectionAdapter.getFilter().filter(charSequence);
            }
        });

        String [] strings = mContext.getResources().getStringArray(R.array.india_states);
        List<String> stateList = new ArrayList<>(Arrays.asList(strings));
        List<CityResponseModel.DataBean> customStateList = new ArrayList<>();
        for(int i=0; i<stateList.size(); i++){
            CityResponseModel.DataBean model = new CityResponseModel.DataBean();
            model.setName(stateList.get(i));
            customStateList.add(model);
        }
        stateSelectionAdapter = new CitySelectionAdapter(mContext,customStateList,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        stateListView.setLayoutManager(mLayoutManager);
        stateListView.setItemAnimator(new DefaultItemAnimator());
        stateListView.addItemDecoration(new ItemDecorator(mContext));
        stateListView.setAdapter(stateSelectionAdapter);
    }

    @OnClick(R.id.closeCityDialog)
    void close(){
        dismiss();
    }

    public void setListener(StateSelectionDialogListener stateSelectionDialogListener){
        this.listener = stateSelectionDialogListener;
    }

    @Override
    public void onCitySelected(CityResponseModel.DataBean state) {
        listener.onStateSelected(state);
        dismiss();
    }

    public interface StateSelectionDialogListener  {
        void onStateSelected(CityResponseModel.DataBean state);
    }
}