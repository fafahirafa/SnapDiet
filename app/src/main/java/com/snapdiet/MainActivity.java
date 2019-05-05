package com.snapdiet;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomnav);
//        BottomNavigationView bottomNav1 = (BottomNavigationView) findViewById(R.id.bottomnav1);
        ImageView cameraNav = (ImageView) findViewById(R.id.navigation_camera);

        BottomNavigationViewHelper.disableShiftMode(bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        bottomNav1.setOnNavigationItemSelectedListener(navListener);
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
