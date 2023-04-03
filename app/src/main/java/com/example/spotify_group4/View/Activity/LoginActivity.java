package com.example.spotify_group4.View.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.LoginFragment;
import com.example.spotify_group4.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements ReplaceFragmentListener {
    ActivityLoginBinding layoutBinding;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityLoginBinding.inflate(getLayoutInflater(), null, false);
        setContentView(layoutBinding.getRoot());
        layoutBinding.topAppBar.setNavigationOnClickListener(v-> onBackPressed());
        initFragment();
        replaceFragment(loginFragment);
    }
    private void initFragment() {
        loginFragment = new LoginFragment();
    }
    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerLogin, fragment);
        if(fragment instanceof LoginFragment){
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void hideComponents() {

    }

    @Override
    public void showComponents() {

    }

    @Override
    public void hideBotNav() {

    }

    @Override
    public void showBotNav() {

    }




}