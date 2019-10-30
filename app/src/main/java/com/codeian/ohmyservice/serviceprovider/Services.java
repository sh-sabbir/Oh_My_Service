package com.codeian.ohmyservice.serviceprovider;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.codeian.ohmyservice.Adapter.MyServicesAdapter;
import com.codeian.ohmyservice.Model.NewService;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Services extends AppCompatActivity {

    private BottomSheetBehavior mBottomSheetBehaviour;
    View nestedScrollView;

    FloatingActionButton addService;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    AutoCompleteTextView serviceSelector;
    TextInputEditText servicePrice,serviceDetails;
    MaterialButton createService;
    RecyclerView serviceRecycler;

    MyServicesAdapter listAdapter;
    Map<String, Object> myServices;
    ArrayAdapter<String> formListAdapter;

    String serviceArea,phoneNumber;

    private String formMode = "new";
    private String editKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_sp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        serviceSelector = findViewById(R.id.serviceSelector);
        servicePrice = findViewById(R.id.servicePrice);
        serviceDetails = findViewById(R.id.serviceDetails);
        createService = findViewById(R.id.createService);
        serviceRecycler = findViewById(R.id.serviceRecycler);
        //serviceRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        serviceRecycler.setLayoutManager(layoutManager);

        myServices = new HashMap<>();

        addService = findViewById(R.id.addNew);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        mBottomSheetBehaviour = BottomSheetBehavior.from(nestedScrollView);


        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBottomSheet();
                clearForm();
            }
        });

        getUserArea();
        getServiceList();
        getMyServices();

        createService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewService();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                clearForm();
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void toggleBottomSheet() {
        if (mBottomSheetBehaviour.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            ObjectAnimator.ofFloat(addService, "rotation", 0f, 45f).setDuration(300).start();
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            ObjectAnimator.ofFloat(addService, "rotation", 45f, 0f).setDuration(300).start();
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    /**
     * Clear focus on touch outside for all EditText inputs.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    private void getServiceList(){

        final ArrayList<String> serviceList = new ArrayList<>();
        mDatabase.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    //Log.d("Service Name:",item_snapshot.getValue().toString());

                    serviceList.add(item_snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        formListAdapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.address_dropdown_layout,
                        serviceList);
        serviceSelector.setAdapter(formListAdapter);
        serviceSelector.setThreshold(100);
    }


    private void getUserArea(){
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData= dataSnapshot.getValue(User.class);

                phoneNumber = user.getPhoneNumber();
                serviceArea = userData.getArea();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void saveNewService(){
        String key = mDatabase.child("listing").push().getKey();

        String name = serviceSelector.getText().toString();
        String price = servicePrice.getText().toString();
        String details = serviceDetails.getText().toString();

        if(formMode.equals("new")) {
            NewService service = new NewService(user.getUid(), phoneNumber, serviceArea, name, price, details);
            //mDatabase.child("listing").child(key).setValue(service);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(key, service);
            mDatabase.child("listing").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        toggleBottomSheet();
                        clearForm();
                        Log.wtf("Create Service", "Done");
                    } else {
                        Log.wtf("Create Service", "createService:onComplete", databaseError.toException());
                    }
                }
            });
        }else{
            String currentItem = editKey;
            HashMap<String, Object> result = new HashMap<>();
            result.put("spService", name);
            result.put("spPrice", price);
            result.put("spDetails", details);

            mDatabase.child("listing").child(currentItem).updateChildren(result, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(Services.this, "Service Updated", Toast.LENGTH_SHORT).show();
                        clearForm();
                        listAdapter.notifyDataSetChanged();
                    }else{
                        Log.wtf("Create Service","createService:onComplete", databaseError.toException());
                    }
                }
            });
        }
    }


    private void getMyServices() {
        mDatabase.child("listing").orderByChild("spID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {

            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myServices.clear();
                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    NewService serviceData = item_snapshot.getValue(NewService.class);
                    key = item_snapshot.getKey();

                    myServices.put(key,serviceData);
                    //Log.i("Service Data:", serviceData.getSpService() +":"+serviceData.getSpPrice());
                }

                listAdapter = new MyServicesAdapter(myServices,Services.this);
                serviceRecycler.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

                Log.d("Adapter","Got hit!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }


    public void adapterDeleteItem(final String key){
        new AlertDialog.Builder(this,R.style.MyDialogTheme)
        .setTitle("Delete Service")
        .setMessage("Do you really want to delete?")

        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                mDatabase.child("listing").child(key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            listAdapter.notifyDataSetChanged();
                            Log.d("Status Change:","Delete");
                        }else{
                            Log.wtf("Delete Service","createService:onComplete", databaseError.toException());
                        }
                    }
                });
            }
        })
        .setNegativeButton(android.R.string.no, null).show();
    }


    public void adapterEditItem(String key, String title,String price, String details){
        formMode = "edit";
        editKey = key;

        serviceSelector.setText(title);
        serviceSelector.setAdapter(null);
        serviceSelector.setClickable(false);
        serviceSelector.setEnabled(false);
        servicePrice.setText(price);
        serviceDetails.setText(details);
        toggleBottomSheet();
    }


    private void clearForm(){
        editKey = null;
        formMode = "new";

        serviceSelector.setText(null);
        serviceSelector.setAdapter(formListAdapter);
        servicePrice.setText(null);
        serviceDetails.setText(null);
    }

    @Override
    public void onBackPressed() {
        clearForm();
        super.onBackPressed();
    }
}
