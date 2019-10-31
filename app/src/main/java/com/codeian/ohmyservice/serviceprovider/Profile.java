package com.codeian.ohmyservice.serviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    TextInputEditText fullName, shopName, details;
    AppCompatAutoCompleteTextView fserviceArea;


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    String status;
    LinearLayout updateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        fullName = findViewById(R.id.fullName);
        shopName = findViewById(R.id.shopName);
        details = findViewById(R.id.serviceDetails);
        updateUser = findViewById(R.id.go_next);

        status = "";

        fserviceArea = findViewById(R.id.selectArea);

        String[] COUNTRIES = new String[] {"Amborkhana", "Zinda Bazar", "Housing Estate", "Bondor", "Mirabazar"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.address_dropdown_layout,
                        COUNTRIES);
        fserviceArea.setAdapter(adapter);
        fserviceArea.setThreshold(100);

        disableViews(fullName,shopName,fserviceArea,details,updateUser);

        DatabaseReference connectedUser = mDatabase.child(user.getUid());
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData= dataSnapshot.getValue(User.class);

                String tempStat = userData.getStatus();
                if(tempStat.equals("2")){
                    status = "2";
                }
                updateUI(userData.getName(),userData.getArea(),userData.getShopName(), userData.getService());
//                Log.d("Response","user data: "+userData.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = fullName.getText().toString();
                String getArea = fserviceArea.getText().toString();
                String getShopName = shopName.getText().toString();
                String getService = details.getText().toString();

                updateProfile(getName,getArea,getShopName,getService);
            }
        });

    }

    private void updateProfile(final String getName, String getArea, String getShopName, String getService) {

        String type = "serviceProvider";

        if(status.isEmpty()){
            status = "1";
        }

        User userData = new User(getName,getArea,getShopName,getService,type,status);

        mDatabase.child(user.getUid())
                .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(getName).build();
                    user.updateProfile(profileUpdates);

                    Intent intent = new Intent(Profile.this, Confirmation.class);
                    startActivity(intent);
                    finish();
                } else {
                    //display a failure message
                    Toast.makeText(Profile.this,"Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUI(String getName, String getArea, String getShopName, String getService){
        fullName.setText(getName);
        fserviceArea.setText(getArea);
        shopName.setText(getShopName);
        details.setText(getService);

        enableViews(fullName,shopName,fserviceArea,details,updateUser);
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
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
