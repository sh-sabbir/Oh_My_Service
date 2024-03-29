package com.codeian.ohmyservice.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeian.ohmyservice.Adapter.OrderListingAdapter;
import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    OrderListingAdapter orderAdapter;
    Map<String, Object> orderList;

    public OrderFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_c_order, container, false);

        recyclerView = root.findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //fetch data and on ServiceListingAdapter

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderList = new HashMap<>();

        getAvailableListing();

        return root;
    }


    private void getAvailableListing() {
        mDatabase.child("orders").orderByChild("customerID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();

                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    OrderModal orderData = item_snapshot.getValue(OrderModal.class);
                    key = item_snapshot.getKey();

                    if(orderData.getStatus().equals("pending")){

                        orderList.put(key,orderData);

                    }
                }

                String count = String.valueOf(orderList.size());

                orderAdapter = new OrderListingAdapter(orderList, getContext());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), History.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}