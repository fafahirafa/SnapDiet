package com.snapdiet;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager, viewPagerText;
    List<Integer> dataImage = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> text = new ArrayList<>();
    ImageView dot1,dot2,dot3;
    Button btnGetStarted;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        viewPager = findViewById(R.id.view_pager);
        btnGetStarted = findViewById(R.id.btn_get_started);

        Intent a = getIntent();
        userId = a.getStringExtra("userid");

        dot1= findViewById(R.id.indicator1);
        dot2= findViewById(R.id.indicator2);
        dot3= findViewById(R.id.indicator3);

        title.add("Calculate your BMI");
        title.add("Capture your Food");
        title.add("Add to Journal");

        text.add("First, calculate your BMI to find out your current mass index");
        text.add("Second, use your camera to find out your food calories");
        text.add("Save your food calories information into a journal to control your calories. Done!");

        dataImage.add(R.drawable.bmi);
        dataImage.add(R.drawable.camera);
        dataImage.add(R.drawable.journal);

        AdapterPager adapterPager = new AdapterPager(this, dataImage, title, text);
        viewPager.setAdapter(adapterPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    dot1.setImageResource(R.drawable.dot_oren);
                    dot2.setImageResource(R.drawable.dot_abu);
                    dot3.setImageResource(R.drawable.dot_abu);
                } else if (i == 1) {
                    dot1.setImageResource(R.drawable.dot_abu);
                    dot2.setImageResource(R.drawable.dot_oren);
                    dot3.setImageResource(R.drawable.dot_abu);
                } else if (i == 2) {
                    dot1.setImageResource(R.drawable.dot_abu);
                    dot2.setImageResource(R.drawable.dot_abu);
                    dot3.setImageResource(R.drawable.dot_oren);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });
    }
}
