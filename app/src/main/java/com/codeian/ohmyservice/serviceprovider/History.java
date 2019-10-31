package com.codeian.ohmyservice.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.codeian.ohmyservice.Adapter.OrderListingAdapterSP;
import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    OrderListingAdapterSP orderAdapter;
    Map<String, Object> orderList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.orderRecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //fetch data and on ServiceListingAdapter

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderList = new HashMap<>();

        getAvailableListing();
    }

    private void getAvailableListing() {
        mDatabase.child("orders").orderByChild("sellerID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();

                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    OrderModal orderData = item_snapshot.getValue(OrderModal.class);
                    key = item_snapshot.getKey();

                    if(!orderData.getStatus().equals("pending")){

                        orderList.put(key,orderData);

                    }
                }

                String count = String.valueOf(orderList.size());

                orderAdapter = new OrderListingAdapterSP(orderList, History.this);
                recyclerView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
                //updateNotice(count);
                Log.d("Adapter","Got hit!");
                Log.d("Total",count+" item");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
