package com.codeian.ohmyservice.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.OrderDetails;
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

public class OrderListingAdapter extends RecyclerView.Adapter<OrderListingAdapter.ViewHolder> {

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


    public OrderListingAdapter(Map<String, Object> availableListing, Context context) {
        mData = new ArrayList();
        mData.addAll(availableListing.entrySet());
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public OrderListingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListingAdapter.ViewHolder viewHolder, final  int i) {

        final Map.Entry<String, Object> item = getItem(i);
        final String orderID,status,time,shopName,shopPhone,serName,serDetails,serCost;
        currentKey = item.getKey();

        final String key = item.getKey();

        System.out.println("Key = " + item.getKey() + ", Value = " + item.getValue());

        OrderModal order = (OrderModal) item.getValue();

        NewService service = order.getOrderService();
        User seller = order.getuServiceProvider();

        orderID = order.getOrderID();
        status = order.getStatus();
        time = order.getTime();

        shopName = seller.getShopName();
        shopPhone = service.getSpPhone();

        serName = service.getSpService();
        serDetails = service.getSpDetails();
        serCost = service.getSpPrice();

        viewHolder.setIsRecyclable(false);

        // hidden Data
        viewHolder.tvName.setText(shopName);
        viewHolder.tvOrderID.setText("#"+orderID);
        viewHolder.tvTime.setText(time);
        viewHolder.tvServiceName.setText(serName);

        //setShopMeta(spID, viewHolder.tvName);
        //setUserRating(spID,viewHolder.tvRating,viewHolder.container);

        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OrderDetails.class);
                intent.putExtra("id",key);
                mContext.startActivity(intent);
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

        private TextView tvName,tvOrderID,tvTime,tvServiceName;
        public View closeBtn;
        public RelativeLayout shopTrigger;
        public LinearLayout expandableLayout;
        public MaterialCardView parentView;
        public MaterialButton orderNow;
        public ShimmerFrameLayout container;

        public ViewHolder(View view) {
            super(view);

            parentView = view.findViewById(R.id.cv);
            tvName = view.findViewById(R.id.tvShopName);
            tvTime = view.findViewById(R.id.timestamp);
            tvOrderID = view.findViewById(R.id.orderID);
            tvServiceName = view.findViewById(R.id.serviceName);

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