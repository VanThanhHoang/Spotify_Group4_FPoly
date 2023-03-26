package com.example.spotify_group4.View.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.SongAdapter;
import com.example.spotify_group4.Listener.GetSongListListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Presenter.PlayListFragmentPresenter;
import com.example.spotify_group4.databinding.FragmentPlaylistBinding;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class PlayListFragment extends Fragment implements GetSongListListener {
    FragmentPlaylistBinding fragmentPlaylistBinding;
    List<Song> mSongList;
    SongAdapter songAdapter;
    private boolean isExpanded = true;
    private boolean isPlaying = false;
    int idPlayList;
    PlayListFragmentPresenter playListFragmentPresenter;

    public PlayListFragment(int idPlayList) {
        this.idPlayList = idPlayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(getLayoutInflater(), container, false);
        initToolbar();
        initToolbarAnimation();
        fragmentPlaylistBinding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pause();
                } else {
                    play();
                }
            }
        });
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void initToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(fragmentPlaylistBinding.toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initToolbarAnimation(){
        fragmentPlaylistBinding.collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.son_tung);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                // setmàu toolbar
                int myColor = palette.getVibrantColor(getResources().getColor(R.color.green));
                fragmentPlaylistBinding.collapsingToolbarLayout.setContentScrimColor(myColor);
                fragmentPlaylistBinding.collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.gray_contain));
            }
        });
        fragmentPlaylistBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)>200){
                    isExpanded = false;
                }else {
                    isExpanded = true;
                }
            }
        });
    }
    private void play() {
        isPlaying = true;
        fragmentPlaylistBinding.playPauseButton.setImageResource(R.drawable.ic_pause);
        // TODO: Start playing audio or video.
    }

    private void pause() {
        isPlaying = false;
        fragmentPlaylistBinding.playPauseButton.setImageResource(R.drawable.ic_play);
        // TODO: Pause playing audio or video.
    }

}
