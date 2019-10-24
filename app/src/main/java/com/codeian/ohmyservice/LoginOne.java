package com.codeian.ohmyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOne extends AppCompatActivity {

    Button btnCustomer,btnServiceProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_one);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnServiceProvider = findViewById(R.id.btnServiceProvider);

        final Intent intent = new Intent(LoginOne.this, LoginTwo.class);

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("userType", "customer");
                startActivity(intent);
                finish();
            }
        });

        btnServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("userType", "serviceProvider");
                startActivity(intent);
                finish();
            }
        });
    }
}
