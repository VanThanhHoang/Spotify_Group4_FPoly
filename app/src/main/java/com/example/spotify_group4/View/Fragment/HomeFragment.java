package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    FragmentHomeBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        replaceFragmentListener = (ReplaceFragmentListener) getContext();
    }

    void initEvent(){
        layoutBinding.btnTest.setOnClickListener(v->
        {
            String url ="https://skymusicfpoly.000webhostapp.com/music/nhunhungphutbandau.mp3";
            replaceFragmentListener.replaceFragment(new MusicPlayFragment(url));
        });
        layoutBinding.btnTest1.setOnClickListener(v->{
            String url ="https://skymusicfpoly.000webhostapp.com/music/baogioanhquen.mp3";
            replaceFragmentListener.replaceFragment(new MusicPlayFragment(url));
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initEvent();
        replaceFragmentListener.showComponents();
        super.onViewCreated(view, savedInstanceState);
    }
}
