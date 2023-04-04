package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.View.Activity.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthPresenter {
    Context context;
    public AuthPresenter(Context context) {
        this.context = context;
    }
    public void insertUser(String userId){
        Call<Void> insertUser = ApiSkyMusic.apiSkyMusic.insertUser(userId);
        insertUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }
    public void goHomeActivity(){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
