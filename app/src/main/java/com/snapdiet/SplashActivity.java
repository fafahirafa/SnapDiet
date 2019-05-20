package com.snapdiet;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    ImageView logoImage, logoText;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoImage = findViewById(R.id.logoImage);
        logoText = findViewById(R.id.logoText);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Auth:", task.getException());
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Auth:", user.getUid());
                    userId = user.getUid();
                    Toast.makeText(SplashActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setFillAfter(true);
        animation.setDuration(2000);
        logoImage.startAnimation(animation);
        logoText.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                intent.putExtra("userid", userId);
                Toast.makeText(SplashActivity.this, userId, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }, 4000);
        //test 
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
