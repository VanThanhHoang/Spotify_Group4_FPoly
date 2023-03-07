package com.example.spotify_group4.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify_group4.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding layoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityLoginBinding.inflate(getLayoutInflater(), null, false);
        setContentView(layoutBinding.getRoot());

    }
}