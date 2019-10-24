package com.codeian.ohmyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.codeian.ohmyservice.customer.Main;
import com.codeian.ohmyservice.customer.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final LinearLayout nxtBtn = findViewById(R.id.go_next);

        firebaseAuth = FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener(){
//            @Override
//            public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if(user!=null){
//                    String name = user.getDisplayName();
//                    if(name == null){
//                        Intent intent = new Intent(Splash.this, Profile.class);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        Intent intent = new Intent(Splash.this, Main.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }else{
//
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    nxtBtn.setVisibility(View.VISIBLE);
//                                }
//                            });
//
//                        }
//                    }, 2000);
//
//                }
//            }
//
//        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String name = user.getDisplayName();
            if(name == null){
                Intent intent = new Intent(Splash.this, Profile.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(Splash.this, Main.class);
                startActivity(intent);
                finish();
            }
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

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //firebaseAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //firebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
