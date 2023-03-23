package com.example.spotify_group4.Presenter;

import com.example.spotify_group4.Listener.LoginListener;
import com.example.spotify_group4.Model.User;

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
