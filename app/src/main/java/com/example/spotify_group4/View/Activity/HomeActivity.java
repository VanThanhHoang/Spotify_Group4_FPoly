package com.example.spotify_group4.View.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.AccountFragment;
import com.example.spotify_group4.View.Fragment.HomeFragment;
import com.example.spotify_group4.View.Fragment.LibraryFragment;
import com.example.spotify_group4.View.Fragment.MusicPlayFragment;
import com.example.spotify_group4.View.Fragment.SearchFragment;
import com.example.spotify_group4.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements ReplaceFragmentListener {
    ActivityHomeBinding layoutBinding;
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    LibraryFragment libraryFragment;
    AccountFragment accountFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }

    void initFragment() {
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        libraryFragment = new LibraryFragment();
        accountFragment = new AccountFragment();
    }

    void initEvent() {
        layoutBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int idItem = item.getItemId();
            if (idItem == R.id.it_Home_MenuHomeNavigation) {
                replaceFragment(homeFragment);
            } else if (idItem == R.id.it_Search_MenuHomeNavigation) {
                replaceFragment(searchFragment);
            } else if (idItem == R.id.it_Library_MenuHomeNavigation) {
                replaceFragment(libraryFragment);
            } else if (idItem == R.id.it_Account_MenuHomeNavigation) {
                replaceFragment(accountFragment);
            }
            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityHomeBinding.inflate(getLayoutInflater(), null, false);
        setContentView(layoutBinding.getRoot());
        initFragment();
        replaceFragment(homeFragment);
        initEvent();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerHome, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.anm_replace_fragment, R.anim.anm_replace_fragment);
        if (fragment instanceof MusicPlayFragment) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void hideComponents() {
        if (layoutBinding.bottomNavigation.isShown()) {
            layoutBinding.bottomNavigation.setVisibility(View.GONE);
        }
    }

    @Override
    public void showComponents() {
        if (!layoutBinding.bottomNavigation.isShown()) {
            layoutBinding.bottomNavigation.setVisibility(View.VISIBLE);
        }
    }
}