package com.codeian.ohmyservice.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.codeian.ohmyservice.R;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.address_dropdown_layout,
                        COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.address_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }
}
