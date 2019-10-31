package com.codeian.ohmyservice.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codeian.ohmyservice.Model.NewService;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.SearchActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceListingAdapter extends RecyclerView.Adapter<ServiceListingAdapter.ViewHolder> {

    private Context mContext;
    private final ArrayList mData;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private List<String> repos;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;
    private User serviceProvider;

    String currentKey;

    private int selected_position = -1;


    public ServiceListingAdapter(Map<String, Object> availableListing, Context context) {
        mData = new ArrayList();
        mData.addAll(availableListing.entrySet());
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public ServiceListingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listing, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServiceListingAdapter.ViewHolder viewHolder, final  int i) {

        final Map.Entry<String, Object> item = getItem(i);
        String spID;
        currentKey = item.getKey();
        System.out.println("Key = " + item.getKey() + ", Value = " + item.getValue());

        final NewService service = (NewService) item.getValue();

        spID = service.getSpID();

        viewHolder.setIsRecyclable(false);

        // hidden Data
        viewHolder.tvPrice.setText(service.getSpPrice()+" BDT");
        viewHolder.serviceDetails.setText(service.getSpDetails());
        viewHolder.container.startShimmer();

        setShopMeta(spID, viewHolder.tvName);
        setUserRating(spID,viewHolder.tvRating,viewHolder.container);

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

        viewHolder.orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SearchActivity)mContext).createOrder(service,serviceProvider);
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
        public MaterialButton orderNow;
        public ShimmerFrameLayout container;

        public ViewHolder(View view) {
            super(view);

            parentView = view.findViewById(R.id.cv);
            shopTrigger = view.findViewById(R.id.shopTrigger);
            closeBtn = view.findViewById(R.id.closeShop);
            tvName = view.findViewById(R.id.tvShopName);
            tvRating = view.findViewById(R.id.tvRating);
            orderNow = view.findViewById(R.id.orderNow);

            container = view.findViewById(R.id.shimmer_view_container);

            serviceDetails = view.findViewById(R.id.serviceDetails);
            tvPrice = view.findViewById(R.id.priceRange);

            expandableLayout = view.findViewById(R.id.expandableLayout);
        }
    }

    private void openCard(TextView tvRating, TextView tvName, MaterialCardView parentView, final LinearLayout expandableLayout, final View closeBtn, final int i) {
        if(expandableLayout.getVisibility() != View.VISIBLE){
            //Do Nothing!
            tvName.setTextColor(context.getResources().getColor(R.color.txtWhite));
            tvRating.setTextColor(context.getResources().getColor(R.color.txtWhite));
            closeBtn.setVisibility(View.VISIBLE);
            expandableLayout.setVisibility(View.VISIBLE);
            parentView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            expandState.put(i, true);
        }else{
//            tvName.setTextColor(context.getResources().getColor(R.color.txtWhite));
//            tvRating.setTextColor(context.getResources().getColor(R.color.txtWhite));
//            closeBtn.setVisibility(View.VISIBLE);
//            expandableLayout.setVisibility(View.VISIBLE);
//            parentView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
//            expandState.put(i, true);
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

    private void setShopMeta(final String spID, final TextView tvName){

        DatabaseReference connectedUser = mDatabase.child("users").child(spID);
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProvider = dataSnapshot.getValue(User.class);

                tvName.setText(serviceProvider.getShopName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void setUserRating(String spID, final TextView tvRating, final ShimmerFrameLayout container){
        DatabaseReference connectedUser = mDatabase.child("avg_rating").child(spID);
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rating;
                if(dataSnapshot.exists()) {
                    rating = dataSnapshot.getValue().toString();
                    if (rating.isEmpty()) {
                        rating = "Not Rated Yet!";
                    }
                }else{
                    rating = "Not Rated Yet!";
                }
                tvRating.setText(rating);
                container.stopShimmer();
                container.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}