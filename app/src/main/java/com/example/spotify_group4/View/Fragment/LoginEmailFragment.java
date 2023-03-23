package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.LoginListener;
import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.Presenter.LoginPresenter;
import com.example.spotify_group4.databinding.LoginEmailBinding;

import java.util.Objects;

public class LoginEmailFragment extends Fragment implements LoginListener {
    LoginEmailBinding layoutBinding;
    private LoginPresenter loginPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = LoginEmailBinding.inflate(inflater, container, false);
        initEvent();
        loginPresenter = new LoginPresenter(this);
        return layoutBinding.getRoot();
    }

    void initEvent() {
        layoutBinding.btnLogin.setOnClickListener(v -> {
            String name = Objects.requireNonNull(layoutBinding.emailInputEditText.getText()).toString();
            String pass = Objects.requireNonNull(layoutBinding.passwordInputEditText.getText()).toString();
            User user = new User(name, pass);
            loginPresenter.logIn(user);
        });
    }

    @Override
    public void onLoginComplete() {
        layoutBinding.emailInputLayout.setError(null);
        layoutBinding.papsswordInputLayout.setError(null);
    }

    @Override
    public void onLoginFail() {
        layoutBinding.emailInputLayout.setError("Email hoặc mật khẩu không hợp lệ");
    }

}

