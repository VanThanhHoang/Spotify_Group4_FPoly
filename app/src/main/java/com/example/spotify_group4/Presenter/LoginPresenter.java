package com.example.spotify_group4.Presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Listener.LoginListener;
import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {
    LoginListener loginListener;

    public LoginPresenter(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
    public void logIn(User user){


        if(user.validateEmail(user.getName()) && user.validatePassword(user.getPassword())){
            loginListener.onLoginComplete();
        }else {
            loginListener.onLoginFail();
        }
    }
}
