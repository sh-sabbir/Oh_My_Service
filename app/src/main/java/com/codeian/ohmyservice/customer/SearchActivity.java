package com.codeian.ohmyservice.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeian.ohmyservice.Adapter.ServiceListingAdapter;
import com.codeian.ohmyservice.Helpers.GenerateRandomString;
import com.codeian.ohmyservice.Model.NewService;
import com.codeian.ohmyservice.Model.OrderModal;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SearchActivity extends AppCompatActivity {

    private String searchTitle,currentArea,serviceArea;

    AutoCompleteTextView areaSelector;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    ServiceListingAdapter listingAdapter;
    Map<String, Object> availableListing;

    RecyclerView listingRecycler;
    TextView serviceNotice;

    User uCustomer,uServiceProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                searchTitle = null;
                currentArea = null;
            } else {
                searchTitle = extras.getString("active_service");
                currentArea = extras.getString("user_address");
            }
        } else {
            searchTitle = (String) savedInstanceState.getSerializable("active_service");
            currentArea = (String) savedInstanceState.getSerializable("user_address");
        }

        getSupportActionBar().setTitle(searchTitle);

        uCustomer = new User();
        uServiceProvider = new User();

        areaSelector = findViewById(R.id.areaSelector);
        String[] Areas = new String[] {"Amborkhana", "Zinda Bazar", "Housing Estate", "Bondor", "Mirabazar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.address_dropdown_layout,Areas);
        areaSelector.setAdapter(adapter);
        areaSelector.setThreshold(100);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        availableListing = new HashMap<>();

        serviceNotice = findViewById(R.id.serviceFound);
        listingRecycler = findViewById(R.id.listingRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listingRecycler.setLayoutManager(layoutManager);

        getUserArea();
        areaSelector.setText(currentArea);


        areaSelector.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serviceArea = areaSelector.getText().toString();
                getAvailableListing();
            }
        });

    }

    private void getUserArea(){
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uCustomer = dataSnapshot.getValue(User.class);
                serviceArea = uCustomer.getArea();

                getAvailableListing();
                Log.d("Area",serviceArea);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void getAvailableListing() {
        mDatabase.child("listing").orderByChild("spArea").equalTo(serviceArea).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                availableListing.clear();

                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    NewService serviceData = item_snapshot.getValue(NewService.class);
                    key = item_snapshot.getKey();

                    if(serviceData.getSpService().equals(searchTitle)){
                        availableListing.put(key,serviceData);
                    }
                }

                String count = String.valueOf(availableListing.size());

                listingAdapter = new ServiceListingAdapter(availableListing, SearchActivity.this);
                listingRecycler.setAdapter(listingAdapter);
                listingAdapter.notifyDataSetChanged();

                updateNotice(count);
                Log.d("Adapter","Got hit!");
                //Log.d("Total",count+" item");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void updateNotice(String count){
        String localArea, localTitle;

        localArea = serviceArea;
        localTitle = searchTitle;

        if(count.equals("0")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String notice = "Sorry! There is no Service provider who provides "+localTitle+" in "+localArea+" Area. Better luck next time!";

                SpannableStringBuilder notFoundNotice = new SpannableStringBuilder();

                notFoundNotice.append("Sorry! There is no Service provider who provides ");
                notFoundNotice.append(localTitle,new StyleSpan(Typeface.BOLD),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                notFoundNotice.append(" in ");
                notFoundNotice.append(localArea,new StyleSpan(Typeface.BOLD),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                notFoundNotice.append(" Area. Better luck next time!");

                serviceNotice.setText(notFoundNotice, TextView.BufferType.SPANNABLE);
            }else{
                String notFoundNotice = "Sorry! There is no Service provider who provides "+localTitle+" in "+localArea+" Area. Better luck next time!";

                serviceNotice.setText(notFoundNotice);
            }

        }else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SpannableStringBuilder foundNotice = new SpannableStringBuilder();
                foundNotice.append("There are ");
                foundNotice.append(count,new StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                foundNotice.append(" Service provider who provides ");
                foundNotice.append(localTitle,new StyleSpan(Typeface.BOLD),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                foundNotice.append(" in ");
                foundNotice.append(localArea,new StyleSpan(Typeface.BOLD),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                foundNotice.append(" Area. Chose yours!");

                serviceNotice.setText(foundNotice);
            }else{
                String foundNotice = "There are " + count + " Service provider who provides " + localTitle + " in " + localArea + " Area. Chose yours!";

                serviceNotice.setText(foundNotice);
            }
        }

    }


    public void createOrder(NewService service, User serviceProvider){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String orderTime = sdf.format(new Date());

        String key = mDatabase.child("listing").push().getKey();
        final String shopName = serviceProvider.getShopName();
        final String orderId= GenerateRandomString.randomString(10);
        final String sellerPhone = service.getSpPhone();

        String cusID = user.getUid();
        String sellerID = service.getSpID();

        OrderModal newOrder = new OrderModal(cusID,sellerID,orderId,orderTime,"pending","",service,uCustomer,serviceProvider);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, newOrder);

        mDatabase.child("orders").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Intent intent = new Intent(SearchActivity.this,Confirmation.class);
                    intent.putExtra("om_id",orderId);
                    intent.putExtra("om_seller",shopName);
                    intent.putExtra("om_phone",sellerPhone);
                    startActivity(intent);
                    //Log.wtf("Create Service", "Done");
                } else {
                    //Log.wtf("Create Service", "createService:onComplete", databaseError.toException());
                }
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
