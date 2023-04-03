package com.example.spotify_group4.View.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify_group4.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        startActivity(intentStartLogInAtv);
        finish();
    }

    private void startHomeActivity() {
        Intent intentStartLogInAtv = new Intent(this, HomeActivity.class);
        startActivity(intentStartLogInAtv);
        finish();
    }

    boolean isUserLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        return currentUser != null || googleSignInAccount != null;
    }
}