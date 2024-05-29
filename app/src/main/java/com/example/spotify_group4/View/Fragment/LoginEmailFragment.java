package com.example.spotify_group4.View.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.LoginListener;
import com.example.spotify_group4.Model.LoginReq;
import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.Presenter.LoginPresenter;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.View.Activity.HomeActivity;
import com.example.spotify_group4.databinding.LoginEmailBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginEmailFragment extends Fragment implements LoginListener {
    LoginEmailBinding layoutBinding;
    private LoginPresenter loginPresenter;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = LoginEmailBinding.inflate(inflater, container, false);
        initEvent();
        loginPresenter = new LoginPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đang đăng nhập");
        return layoutBinding.getRoot();

    }
    void initEvent() {
        layoutBinding.btnLogin.setOnClickListener(v -> {
            progressDialog.show();
            String name = Objects.requireNonNull(layoutBinding.emailInputEditText.getText()).toString();
            String pass = Objects.requireNonNull(layoutBinding.passwordInputEditText.getText()).toString();
            Call<LoginReq> callLogin = ApiSkyMusic.apiSkyMusic.login(name, pass);
            callLogin.enqueue(new Callback<LoginReq>() {
                @Override
                public void onResponse(@NonNull Call<LoginReq> call, @NonNull Response<LoginReq> response) {
                   LoginReq loginReq = response.body();
                  try {
                      if(loginReq != null) {
                          if (loginReq.getStatus().equals("success")) {
                              SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", loginReq.getEmail());
                                editor.apply();
                                Log.d("LoginPresenter", "onResponse: " + loginReq.getEmail());
                                startActivity(new Intent(getContext(), HomeActivity.class));
                          } else {
                              AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                              builder.setTitle("Thông báo");
                              builder.setMessage("Đăng nhập thất bại");
                              builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                              builder.show();
                          }
                      }
                  }catch (Exception e){
                      AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Đăng nhập thất bại");
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        builder.show();
                      Log.d("LoginPresenter", "onResponse: " + e.getMessage());
                  }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<LoginReq> call, @NonNull Throwable t) {
                Log.d("LoginPresenter", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
                }
            }   );
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

