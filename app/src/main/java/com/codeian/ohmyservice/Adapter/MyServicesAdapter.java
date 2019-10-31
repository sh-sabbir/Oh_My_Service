package com.codeian.ohmyservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeian.ohmyservice.Model.NewService;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.serviceprovider.Services;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.ViewHolder> {

    private Context context;
    private Context mContext;
    private final ArrayList mData;
    private DatabaseReference mDatabase;

    public MyServicesAdapter(Map<String, Object> myServices, Context context) {
        mData = new ArrayList();
        mData.addAll(myServices.entrySet());
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_services, parent, false);

        return new MyServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyServicesAdapter.ViewHolder holder, final int position) {

        final Map.Entry<String, Object> item = getItem(position);

        //System.out.println("Key = " + item.getKey() + ", Value = " + item.getValue());

        final NewService service = (NewService) item.getValue();
        holder.tvServiceName.setText(service.getSpService());
        holder.tvPrice.setText(service.getSpPrice());
        holder.serviceDetails.setText(service.getSpDetails());


        holder.dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Services)mContext).adapterDeleteItem(item.getKey());
            }
        });

        holder.editBdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Services)mContext).adapterEditItem(item.getKey(),service.getSpService(),"10",service.getSpDetails());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public Map.Entry<String, Object> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvServiceName,serviceDetails,tvPrice;
        public MaterialButton dltBtn, editBdt;

        public ViewHolder(View view) {
            super(view);
            dltBtn = view.findViewById(R.id.actionDlt);
            editBdt = view.findViewById(R.id.actionEdit);

            tvServiceName = view.findViewById(R.id.tvServiceName);
            serviceDetails = view.findViewById(R.id.mServiceDetails);
            tvPrice = view.findViewById(R.id.tvPrice);
        }
    }

}
