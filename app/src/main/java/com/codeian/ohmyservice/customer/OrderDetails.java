package com.codeian.ohmyservice.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codeian.ohmyservice.Adapter.OrderListingAdapter;
import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.serviceprovider.Services;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {

    String bOrderID,phoneNumber;
    TextView orderStatus, serName,serCost,dateTime,infoOrderId,infoShopName,ratingData;
    MaterialButton actionCall,actionRating;
    private DatabaseReference mDatabase;

    CardView feedback;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bOrderID = null;
            } else {
                bOrderID = extras.getString("id");
            }
        } else {
            bOrderID = (String) savedInstanceState.getSerializable("id");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderStatus =findViewById(R.id.orderStatus);
        serName = findViewById(R.id.serName);
        serCost = findViewById(R.id.serCost);
        dateTime = findViewById(R.id.dateTime);
        infoShopName = findViewById(R.id.infoShopName);
        infoOrderId = findViewById(R.id.infoOrderId);
        feedback = findViewById(R.id.feedback);
        ratingBar = findViewById(R.id.starRating);
        ratingData = findViewById(R.id.ratingData);

        actionRating = findViewById(R.id.actionRating);
        actionCall = findViewById(R.id.actionCall);
        actionCall.setEnabled(false);
        Log.d("Order_ID",bOrderID);

        loadOrderData();

        actionCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
            }
        });

        actionRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveRating();
            }
        });
    }

    private void loadOrderData() {
        mDatabase.child("orders").child(bOrderID).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status,rating;
                OrderModal orderData = dataSnapshot.getValue(OrderModal.class);

                infoOrderId.setText(orderData.getOrderID());
                infoShopName.setText(orderData.getuServiceProvider().getShopName());
                orderStatus.setText(orderData.getStatus());

                status = orderData.getStatus();
                rating = orderData.getRating();

                dateTime.setText("Order Time: "+orderData.getTime());
                serName.setText("Service Name: "+orderData.getOrderService().getSpService());
                serCost.setText("Service Cost: "+orderData.getOrderService().getSpPrice()+"BDT");
                phoneNumber = orderData.getOrderService().getSpPhone();
                actionCall.setEnabled(true);

                if(status.equals("reject")){
                    actionCall.setVisibility(View.GONE);
                }

                if(status.equals("complete")){
                    actionCall.setVisibility(View.GONE);
                    feedback.setVisibility(View.VISIBLE);
                }

                if(!rating.isEmpty()){
                    feedback.setVisibility(View.GONE);
                    ratingData.setText("Rating: "+rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }


    private void giveRating(){
        String rating = String.valueOf(ratingBar.getRating());
        mDatabase.child("orders").child(bOrderID).child("rating").setValue(rating);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
