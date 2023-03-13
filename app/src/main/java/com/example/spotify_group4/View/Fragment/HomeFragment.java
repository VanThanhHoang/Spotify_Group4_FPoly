package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.PlaylistAdapter;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FragmentHomeBinding layoutBinding;
    List<PlayList> playLists;
    RecyclerView[] listDefaultPlayList;
    ReplaceFragmentListener replaceFragmentListener;

    void createPlayListRecycleView() {
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(playLists,replaceFragmentListener);
        for (int i = 0; i < 5; i++) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            listDefaultPlayList[i].setLayoutManager(linearLayoutManager);
            listDefaultPlayList[i].setAdapter(playlistAdapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return layoutBinding.getRoot();
    }



    void createPlayLists() {
        playLists = new ArrayList<>();
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
        playLists.add(new PlayList("Tuyển tập nhạc Orijinn", "https://i.scdn.co/image/ab67616d0000b273e543a517bf685934d8688e27"));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        replaceFragmentListener = (ReplaceFragmentListener) getContext();
    }

    void initEvent() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createPlayLists();
        initEvent();
        listDefaultPlayList = new RecyclerView[]{
                layoutBinding.rvPlayListDefault0,
                layoutBinding.rvPlayListDefault1,
                layoutBinding.rvPlayListDefault2,
                layoutBinding.rvPlayListDefault3,
                layoutBinding.rvPlayListDefault4,
        };
        createPlayListRecycleView();
        replaceFragmentListener.showComponents();
        super.onViewCreated(view, savedInstanceState);
    }
}
