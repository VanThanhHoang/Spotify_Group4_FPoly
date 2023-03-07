package com.example.spotify_group4.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.spotify_group4.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler(Looper.getMainLooper()).postDelayed(this::startLoginActivity, 3000);
    }
    private void startLoginActivity(){
        Intent intentStartLogInAtv = new Intent(this,LoginActivity.class);
        finish();
        startActivity(intentStartLogInAtv);
    }
}