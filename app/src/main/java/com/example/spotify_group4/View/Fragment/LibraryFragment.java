package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.databinding.FragmentLibraryBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LibraryFragment extends Fragment {
    FragmentLibraryBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentLibraryBinding.inflate(getLayoutInflater(), container, false);

        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceFragmentListener.showComponents();
        getUserId();
        setEvent();
    }

    void setEvent() {
        layoutBinding.btnSongLiked.setOnClickListener(v -> replaceFragmentListener.replaceFragment(new PlayListFragment(userId)));
        layoutBinding.btnMyPlayList.setOnClickListener(v -> replaceFragmentListener.replaceFragment(new UserPlayListFragment()));
    }

    void getUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", 0);
        userId = sharedPreferences.getString("token", "");
    }

}
