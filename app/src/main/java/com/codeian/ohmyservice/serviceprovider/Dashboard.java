package com.codeian.ohmyservice.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codeian.ohmyservice.LoginOne;
import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.Main;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    MaterialCardView orders,history,service,profile,review,logut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Dashboard");

        mAuth = FirebaseAuth.getInstance();

        orders = findViewById(R.id.lOrders);
        history = findViewById(R.id.lHistory);
        service = findViewById(R.id.lServices);
        profile = findViewById(R.id.lProfile);
        review = findViewById(R.id.lReview);
        logut = findViewById(R.id.lLogout);

        orders.setOnClickListener(this);
        history.setOnClickListener(this);
        service.setOnClickListener(this);
        profile.setOnClickListener(this);
        review.setOnClickListener(this);
        logut.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sp_dashboard, menu);
        return true;
    }


    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(Dashboard.this, LoginOne.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lOrders:
                makeToast("Order Cliked");
                break;

            case R.id.lHistory:
                makeToast("History Cliked");
                break;

            case R.id.lServices:
                makeToast("Services Cliked");
                break;

            case R.id.lProfile:
                makeToast("Profile Cliked");
                break;

            case R.id.lReview:
                makeToast("Review Cliked");
                break;

            case R.id.lLogout:
                signOut();
                break;
        }
    }

    private void makeToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}
