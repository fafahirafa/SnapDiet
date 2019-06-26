package com.snapdiet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    ImageView cameraNav;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userId;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent a = getIntent();
        userId = a.getStringExtra("userid");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("journal");

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomnav);

        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        /**
         * 30-04-2019
         * Start - Add Home Fragment
         */
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new HomeFragment(), "SOMETAG").
                    commit();
        }
        /**
         * 30-04-2019
         * End - Add Home Fragment
         */

    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_camera:
                        selectedFragment = new CameraFragment();
                        break;
                    case R.id.navigation_tips:
                        selectedFragment = new TipsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        };

}
