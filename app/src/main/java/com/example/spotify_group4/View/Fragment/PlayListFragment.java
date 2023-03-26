package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.SongAdapter;
import com.example.spotify_group4.Listener.GetSongListListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.PlayListFragmentPresenter;
import com.example.spotify_group4.databinding.FragmentPlaylistBinding;

import java.util.List;

public class PlayListFragment extends Fragment implements GetSongListListener {
    FragmentPlaylistBinding fragmentPlaylistBinding;
    List<Song> mSongList;
    SongAdapter songAdapter;
    int idPlayList;
    PlayListFragmentPresenter playListFragmentPresenter;

    public PlayListFragment(int idPlayList) {
        this.idPlayList = idPlayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(getLayoutInflater(), container, false);
        return fragmentPlaylistBinding.getRoot();
    }

    void createRecycleView() {
        songAdapter = new SongAdapter(mSongList, (ReplaceFragmentListener) getContext());
        RecyclerView.ItemDecoration itemDecoration = null;
        if (getContext() != null) {
            itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        }
        fragmentPlaylistBinding.rvSong.addItemDecoration(itemDecoration);
        fragmentPlaylistBinding.rvSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentPlaylistBinding.rvSong.setAdapter(songAdapter);
    }

    void createListMusic() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        playListFragmentPresenter = new PlayListFragmentPresenter(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        playListFragmentPresenter.getSongListByPlayListId(idPlayList);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGetSongListComplete(List<Song> songList) {
        mSongList = songList;
        createListMusic();
        createRecycleView();
    }

    @Override
    public void onGetSongListFail() {

    }
}
