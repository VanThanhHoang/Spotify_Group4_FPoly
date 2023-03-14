package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotify_group4.Adapter.SongAdapter;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.databinding.FragmentPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends Fragment {
    FragmentPlaylistBinding fragmentPlaylistBinding;
    List<Song> songList;
    SongAdapter songAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(getLayoutInflater(), container, false);
        return fragmentPlaylistBinding.getRoot();
    }

    void createRecycleView() {
        songAdapter = new SongAdapter(songList, (ReplaceFragmentListener) getContext());
        fragmentPlaylistBinding.rvSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentPlaylistBinding.rvSong.setAdapter(songAdapter);
    }

    void createListMusic() {
        songList = new ArrayList<>();
        songList.add(new Song("Ngủ sớm đi em","DucMinh","https://firebasestorage.googleapis.com/v0/b/spotifygroup4-7c9b7.appspot.com/o/ngusomdiem.mp3?alt=media&token=fb5634e3-9f20-41c6-b89c-70c9446c924e","https://avatar-ex-swe.nixcdn.com/song/share/2022/10/10/3/2/0/d/1665384633151.jpg"));
        songList.add(new Song("Chỉ một đêm nữa thôi","RPT MCK","https://firebasestorage.googleapis.com/v0/b/spotifygroup4-7c9b7.appspot.com/o/chimotdemnuathoi.mp3?alt=media&token=4b9acfba-63e0-4365-aaff-09cd46eb07f5","https://i.ytimg.com/vi/kNb9k7bNtf4/maxresdefault.jpg"));
        songList.add(new Song("Em là kẻ đáng thương", "Phát Huy T4", "https://firebasestorage.googleapis.com/v0/b/spotifygroup4-7c9b7.appspot.com/o/emlakedangthuong.mp3?alt=media&token=4d9b1d2a-af1d-4822-8bfe-75b57bd1014a", "https://images2.thanhnien.vn/528068263637045248/2023/2/6/base64-1675694187028671676543.png"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://firebasestorage.googleapis.com/v0/b/spotifygroup4-7c9b7.appspot.com/o/xinloivacamon.mp3?alt=media&token=05dd8762-5dfe-4d0f-ba0b-07a6e6921de2", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfAmW0YZGBP6WpFXW34wMegcWOpCJwOwj3lJwDGzfpypuw&oe=64368F55"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createListMusic();
        createRecycleView();
        super.onViewCreated(view, savedInstanceState);
    }
}
