package com.codeian.ohmyservice.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codeian.ohmyservice.R;
import com.google.android.material.button.MaterialButton;

public class Confirmation extends AppCompatActivity {

    MaterialButton actionCall,actionHome;
    TextView shopName,orderID;
    String bShopName, bOrderID,bShopPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirmation");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bShopName = null;
                bOrderID = null;
                bShopPhone = null;
            } else {
                bShopName = extras.getString("om_seller");
                bOrderID = extras.getString("om_id");
                bShopPhone = extras.getString("om_phone");
            }
        } else {
            bShopName = (String) savedInstanceState.getSerializable("om_seller");
            bOrderID = (String) savedInstanceState.getSerializable("om_id");
            bShopPhone = (String) savedInstanceState.getSerializable("om_phone");
        }

        actionCall = findViewById(R.id.actionCall);
        actionHome = findViewById(R.id.actionHome);

        shopName = findViewById(R.id.infoShopName);
        orderID = findViewById(R.id.infoOrderId);

        shopName.setText(bShopName);
        orderID.setText(bOrderID);

        actionCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+bShopPhone));
                startActivity(intent);

            }
        });

        actionHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Confirmation.this,Main.class);
                startActivity(intent);
                finish();
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
