package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.databinding.FragmentLibraryBinding;

public class LibraryFragment extends Fragment {
    FragmentLibraryBinding layoutBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentLibraryBinding.inflate(getLayoutInflater(),container,false);
        return layoutBinding.getRoot();
    }
}
