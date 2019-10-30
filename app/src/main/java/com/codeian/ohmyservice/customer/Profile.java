package com.codeian.ohmyservice.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codeian.ohmyservice.LoginTwo;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
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

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    TextInputEditText fullName,addressTxt;
    AppCompatAutoCompleteTextView area;

    LinearLayout updateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        fullName = findViewById(R.id.fullName);
        area = findViewById(R.id.address_dropdown);
        addressTxt = findViewById(R.id.addressTxt);
        updateUser = findViewById(R.id.go_next);


        String[] COUNTRIES = new String[] {"Amborkhana", "Zinda Bazar", "Housing Estate", "Bondor", "Mirabazar"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.address_dropdown_layout,
                        COUNTRIES);
        area.setAdapter(adapter);
        area.setThreshold(100);

        disableViews(fullName,area,addressTxt,updateUser);

        DatabaseReference connectedUser = mDatabase.child(user.getUid());
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData= dataSnapshot.getValue(User.class);

                updateUI(userData.getName(),userData.getArea(),userData.getAddress());
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
                String getArea = area.getText().toString();
                String getAddress = addressTxt.getText().toString();

                updateProfile(getName,getArea,getAddress);
            }
        });
    }

    private void updateProfile(final String getName, String getArea, String getAddress) {

        String type = "1";
        String status = "1";
        User userData = new User(getName,getArea,getAddress,type,status);

        mDatabase.child(user.getUid())
                .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(getName).build();
                    user.updateProfile(profileUpdates);

                    Intent intent = new Intent(Profile.this, Main.class);
                    startActivity(intent);
                    finish();
                } else {
                    //display a failure message
                    Toast.makeText(Profile.this,"Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUI(String getName, String getArea, String getAddres){
        fullName.setText(getName);
        area.setText(getArea);
        addressTxt.setText(getAddres);

        enableViews(fullName,area,addressTxt,updateUser);
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
}
