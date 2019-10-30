package com.codeian.ohmyservice.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codeian.ohmyservice.Model.NewService;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.SearchActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableRecyclerAdapter extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private final ArrayList mData;
    private DatabaseReference mDatabase;

    private List<String> repos;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;

    String currentKey;

    private int selected_position = -1;

//    public ExpandableRecyclerAdapter(List<String> repos) {
//        this.repos = repos;
//        //set initial expanded state to false
//        for (int i = 0; i < repos.size(); i++) {
//            expandState.append(i, false);
//        }
//    }

    public ExpandableRecyclerAdapter(Map<String, Object> availableListing, Context context) {
        mData = new ArrayList();
        mData.addAll(availableListing.entrySet());
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public ExpandableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExpandableRecyclerAdapter.ViewHolder viewHolder, final  int i) {

        final Map.Entry<String, Object> item = getItem(i);

        currentKey = item.getKey();
        System.out.println("Key = " + item.getKey() + ", Value = " + item.getValue());

        final NewService service = (NewService) item.getValue();

        viewHolder.setIsRecyclable(false);

        viewHolder.tvName.setText("Handy Repair Store");
        viewHolder.tvRating.setText("4.8 (85 Reviews)");

        // hidden Data
        viewHolder.tvPrice.setText(service.getSpPrice()+" BDT");
        viewHolder.serviceDetails.setText(service.getSpDetails());

        //check if view is expanded
        final boolean isExpanded = expandState.get(i);

        viewHolder.expandableLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.shopTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openCard(viewHolder.tvRating, viewHolder.tvName,viewHolder.parentView, viewHolder.expandableLayout, viewHolder.closeBtn,  i);
            }
        });

        viewHolder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                closeCard(viewHolder.tvRating, viewHolder.tvName,viewHolder.parentView, viewHolder.expandableLayout, viewHolder.closeBtn,  i);
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

        private TextView tvName,serviceDetails,tvPrice,tvRating;
        public View closeBtn;
        public RelativeLayout shopTrigger;
        public LinearLayout expandableLayout;
        public MaterialCardView parentView;

        public ViewHolder(View view) {
            super(view);

            parentView = view.findViewById(R.id.cv);
            shopTrigger = view.findViewById(R.id.shopTrigger);
            closeBtn = view.findViewById(R.id.closeShop);
            tvName = view.findViewById(R.id.tvShopName);
            tvRating = view.findViewById(R.id.tvRating);

            serviceDetails = view.findViewById(R.id.serviceDetails);
            tvPrice = view.findViewById(R.id.priceRange);

            expandableLayout = view.findViewById(R.id.expandableLayout);
        }
    }

    private void openCard(TextView tvRating, TextView tvName, MaterialCardView parentView, final LinearLayout expandableLayout, final View closeBtn, final int i) {
        if (expandableLayout.getVisibility() == View.VISIBLE){
            //Do Nothing!
        }else{
            tvName.setTextColor(context.getResources().getColor(R.color.txtWhite));
            tvRating.setTextColor(context.getResources().getColor(R.color.txtWhite));
            closeBtn.setVisibility(View.VISIBLE);
            expandableLayout.setVisibility(View.VISIBLE);
            parentView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            expandState.put(i, true);
        }
    }

    private void closeCard(TextView tvRating, TextView tvName, MaterialCardView parentView, final LinearLayout expandableLayout, final View closeBtn, final int i) {
        if (expandableLayout.getVisibility() == View.VISIBLE){
            tvName.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            tvRating.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            closeBtn.setVisibility(View.GONE);
            expandableLayout.setVisibility(View.GONE);
            parentView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            expandState.put(i, false);
        }
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}