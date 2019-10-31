package com.codeian.ohmyservice.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.codeian.ohmyservice.LoginOne;
import com.codeian.ohmyservice.R;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_sp);


        LinearLayout backToLogin = findViewById(R.id.backToLogin);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Confirmation.this, LoginOne.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
