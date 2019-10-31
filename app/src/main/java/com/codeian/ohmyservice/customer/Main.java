package com.codeian.ohmyservice.customer;

import android.content.Intent;
import android.os.Bundle;

import com.codeian.ohmyservice.LoginOne;
import com.codeian.ohmyservice.Model.User;
import com.codeian.ohmyservice.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;


public class Main extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration,bottomBarConfiguration;
    private Toolbar toolbar;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseUser user;
    private DatabaseReference mDatabase;


    ImageView goToProfile;
    TextView userName, userRating;

    private String uArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        final BottomNavigationView bottomAppBar = findViewById(R.id.bottom_nav);

        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_order, R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_complaints || destination.getId() == R.id.nav_address || destination.getId() == R.id.nav_review) {
                    bottomAppBar.setVisibility(View.GONE);
                } else {
                    bottomAppBar.setVisibility(View.VISIBLE);
                }
            }
        });

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                signOut();
                return false;
            }
        });

        View hView =  navigationView.getHeaderView(0);
        goToProfile = hView.findViewById(R.id.goToProfile);
        userName = hView.findViewById(R.id.userName);
        userRating = hView.findViewById(R.id.userRating);

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Profile.class);
                startActivity(intent);
            }
        });

        setUserMeta();
        setUserRating();
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(Main.this, LoginOne.class);
        startActivity(intent);
        finish();
    }

    private void setUserMeta(){
        DatabaseReference connectedUser = mDatabase.child("users").child(user.getUid());
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData= dataSnapshot.getValue(User.class);

                userName.setText(userData.getName());
                uArea = userData.getArea();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void setUserRating(){
        DatabaseReference connectedUser = mDatabase.child("avg_rating").child(user.getUid());
        connectedUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String rating = (String) dataSnapshot.getValue().toString();
                    userRating.setText(rating);
                }else{
                    userRating.setText("No");
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    toolbar.setTitle("Shop");
                    return true;
                case R.id.nav_order:
                    toolbar.setTitle("My Gifts");
                    return true;
                case R.id.nav_help:
                    toolbar.setTitle("Cart");
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
