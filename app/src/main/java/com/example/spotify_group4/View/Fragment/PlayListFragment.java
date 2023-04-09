package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.spotify_group4.Listener.LoadListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.PlayListFragmentPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.databinding.FragmentPlaylistBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayListFragment extends Fragment implements GetSongListListener {
    FragmentPlaylistBinding fragmentPlaylistBinding;
    List<Song> mSongList;
    SongAdapter songAdapter;
    private boolean isPlaying;
    PlayList mPlayList;
    LoadListener loadListener;
    PlayListFragmentPresenter playListFragmentPresenter;
    ReplaceFragmentListener replaceFragmentListener;
    Singer singer;
    String userId;
    public static final String FLAG_USER_PLAYLIST = "FLAG_USER_PLAYLIST";
    String flag;

    public PlayListFragment(PlayList playList) {
        mPlayList = playList;
    }

    public PlayListFragment(PlayList playList, String flag) {
        this.flag = flag;
        mPlayList = playList;
    }

    public PlayListFragment(Singer singer) {
        this.singer = singer;
    }

    public PlayListFragment(String userId) {
        this.userId = userId;
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

        if (itemDecoration != null) {
            fragmentPlaylistBinding.rvSong.addItemDecoration(itemDecoration);
            fragmentPlaylistBinding.rvSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            fragmentPlaylistBinding.rvSong.setAdapter(songAdapter);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        playListFragmentPresenter = new PlayListFragmentPresenter(this);
        loadListener = (LoadListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadListener.onLoad();
        if (flag != null) {
            if (mPlayList != null && !flag.isEmpty()) {
                playListFragmentPresenter.getSongByUserPlayList(mPlayList.getId());
            }
        } else if (singer != null) {
            playListFragmentPresenter.getSongListBySingerId(singer.getId());
            Picasso.get().load(singer.getUrlImg()).into(fragmentPlaylistBinding.imgPlayList);
        } else if (userId != null) {
            playListFragmentPresenter.getSongLiked(userId);
        } else {
            playListFragmentPresenter.getSongListByPlayListId(mPlayList.getId());
            Picasso.get().load(mPlayList.getUrlImg()).into(fragmentPlaylistBinding.imgPlayList);
        }
        initToolbar();
        initToolbarAnimation();
        fragmentPlaylistBinding.playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pause();
            } else {
                play();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onGetSongListComplete(List<Song> songList) {
        mSongList = songList;
        loadListener.onComplete();
        createRecycleView();
        if ( userId != null  ) {
            fragmentPlaylistBinding.collapsingToolbarLayout.setTitle("Bài hát đã thích");
            Picasso.get().load(mSongList.get(0).getUrlImg()).into(fragmentPlaylistBinding.imgPlayList);
        }
        if (flag!=null){
            fragmentPlaylistBinding.collapsingToolbarLayout.setTitle(mPlayList.getName());
            Picasso.get().load(mSongList.get(0).getUrlImg()).into(fragmentPlaylistBinding.imgPlayList);
        }
    }

    @Override
    public void onGetSongListFail() {
        loadListener.onComplete();
        Toast.makeText(getContext(), "Không có bài hát nào trong playlist", Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(fragmentPlaylistBinding.toolbar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void initToolbarAnimation() {
        if (mPlayList != null) {
            fragmentPlaylistBinding.collapsingToolbarLayout.setTitle(mPlayList.getName());
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.son_tung);
        Palette.from(bitmap).generate(palette -> {
            // set màu toolbar
            fragmentPlaylistBinding.collapsingToolbarLayout.setContentScrimColor(requireContext().getResources().getColor(R.color.black, null));
            fragmentPlaylistBinding.collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.black, null));
        });
    }

    private void play() {
        isPlaying = true;
        fragmentPlaylistBinding.playPauseButton.setImageResource(R.drawable.ic_pause);
        replaceFragmentListener.replaceFragment(new MusicPlayFragment(mSongList, 0));
    }

    private void pause() {
        isPlaying = false;
        fragmentPlaylistBinding.playPauseButton.setImageResource(R.drawable.ic_play);
    }

}
