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
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
        songList.add(new Song("Xinloivacamon \uD83D\uDE4F\uD83C\uDFFB (Beautiful day remix)", "RPT ORIJINN", "https://skymusicfpoly.000webhostapp.com/music/xinloivacamon.mp3", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t1.6435-9/119194313_377100417020243_132281503110072710_n.jpg?stp=dst-jpg_p843x403&_nc_cat=105&ccb=1-7&_nc_sid=730e14&_nc_ohc=sQL2541ewMwAX8Yf0bq&_nc_oc=AQl3cDznBKoBbqP7b6PL63gFTJgjl4jg3Qf_haiUibYpS6yusNj8aXW70rDOZ6HH8KQfnSO0Dgk2rO_wnVp4C7gz&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfCg3JQEYPeBx8m-iwbiErLolznF-W9qUtupUZqvJJ6fSQ&oe=64368F55"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createListMusic();
        createRecycleView();
        super.onViewCreated(view, savedInstanceState);
    }
}
