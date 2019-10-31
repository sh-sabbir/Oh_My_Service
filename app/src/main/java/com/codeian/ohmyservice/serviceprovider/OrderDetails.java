package com.codeian.ohmyservice.serviceprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity {

    String bOrderID,phoneNumber;
    TextView orderStatus, serName,serCost,dateTime,infoOrderId,infoShopName,cArea,cAddress,orderRating;
    MaterialButton actionCall,actionDone,actionReject;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_sp);

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
        cArea = findViewById(R.id.cArea);
        cAddress = findViewById(R.id.cAddress);
        orderRating = findViewById(R.id.orderRating);

        actionCall = findViewById(R.id.actionCall);
        actionDone = findViewById(R.id.actionDone);
        actionReject = findViewById(R.id.actionReject);

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

        actionDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("complete");
            }
        });

        actionReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("reject");
            }
        });
    }

    private void updateStatus(String status) {
        mDatabase.child("orders").child(bOrderID).child("status").setValue(status);
    }

    private void loadOrderData() {
        mDatabase.child("orders").child(bOrderID).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String status,rating;

                OrderModal orderData = dataSnapshot.getValue(OrderModal.class);

                infoOrderId.setText(orderData.getOrderID());
                infoShopName.setText(orderData.getOrderService().getSpService());
                orderStatus.setText(orderData.getStatus());

                status = orderData.getStatus();
                rating = orderData.getRating();

                dateTime.setText("Order Time: "+orderData.getTime());
                serName.setText("Customer Name: "+orderData.getuCustomer().getName());
                serCost.setText("Service Cost: "+orderData.getOrderService().getSpPrice()+"BDT");

                cArea.setText("Area: "+orderData.getuCustomer().getArea());
                cAddress.setText("Address: "+orderData.getuCustomer().getAddress());

                phoneNumber = orderData.getOrderService().getSpPhone();
                actionCall.setEnabled(true);

                if(status.equals("reject") || status.equals("complete")){
                    actionCall.setVisibility(View.GONE);
                    actionDone.setVisibility(View.GONE);
                    actionReject.setVisibility(View.GONE);
                }else{
                    actionCall.setVisibility(View.VISIBLE);
                    actionDone.setVisibility(View.VISIBLE);
                    actionReject.setVisibility(View.VISIBLE);
                }

                if(!rating.isEmpty()){
                    orderRating.setText("Rating: "+rating);
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
