package com.codeian.ohmyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.customer.Main;
import com.codeian.ohmyservice.serviceprovider.Confirmation;
import com.codeian.ohmyservice.serviceprovider.Dashboard;
import com.codeian.ohmyservice.serviceprovider.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final LinearLayout nxtBtn = findViewById(R.id.go_next);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        if(user!=null){
            checkUserType();
        }else{

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nxtBtn.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }, 2000);

        }

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash.this, LoginOne.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkUserType(){
        DatabaseReference connectedUser = mDatabase.child(user.getUid());
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData= dataSnapshot.getValue(User.class);

                String uType, uName, uArea, uShopName, uStatus;
                uType = userData.getType();
                uName = userData.getName();
                uArea = userData.getArea();

                if(uType.equals("serviceProvider")){
                    uShopName = userData.getShopName();
                    uStatus   = userData.getStatus();

                    redirectUser(uType, uName, uArea, uShopName, uStatus);
                }else{
                    redirectUser(uType, uName, uArea, uShopName = null, uStatus = null);
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

    private void redirectUser(String type, String uName, String uArea, String uShopName, String uStatus){

        // User Status Guide (Service provider)
        // 0 = profile not complete
        // 1 = profile complete but not approved
        // 2 = approved


        if(type.equals("serviceProvider") && uStatus.equals("0")){
            Intent intent = new Intent(Splash.this, Profile.class);
            startActivity(intent);
            finish();
        }else if(type.equals("serviceProvider") && uStatus.equals("1")){
            Intent intent = new Intent(Splash.this, Confirmation.class);
            startActivity(intent);
            finish();
        }else if(type.equals("serviceProvider") && uStatus.equals("2")){
            Intent intent = new Intent(Splash.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(Splash.this, Main.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
