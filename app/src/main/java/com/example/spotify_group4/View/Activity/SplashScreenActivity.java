package com.example.spotify_group4.View.Activity;

import static com.example.spotify_group4.Retrofit.ApiSkyMusic.apiSkyMusic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isUserLogin()) {
                startHomeActivity();
            } else {
                startLoginActivity();
            }
        }, 3000);
    }

    private void startLoginActivity() {
        Intent intentStartLogInAtv = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intentStartLogInAtv);
    }

    private void startHomeActivity() {
        Intent intentStartLogInAtv = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intentStartLogInAtv);
    }

    boolean isUserLogin() {
        FirebaseUser currentUser = null;
        try {
            currentUser = mAuth.getCurrentUser();
        } catch (Exception e) {
            e.getMessage();
        }
        return currentUser != null;
    }
}