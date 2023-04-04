package com.example.spotify_group4.View.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotify_group4.Adapter.SingerAdapter;
import com.example.spotify_group4.Adapter.SongAdapter;
import com.example.spotify_group4.Listener.LoadListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Listener.SearchListener;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.SearchPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.databinding.FragmentSearchBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchListener {
    FragmentSearchBinding layoutBinding;
    SearchPresenter searchPresenter;
    SongAdapter songAdapter;
    SingerAdapter singerAdapter;
    private static final int STATE_SEARCH_SONG = 0;
    private static final int STATE_SEARCH_SINGER = 1;
    int searchState;
    ReplaceFragmentListener replaceFragmentListener;
    LoadListener loadListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        searchPresenter = new SearchPresenter(this);
        return layoutBinding.getRoot();
    }

    void applySearchState(int newSearchState) {
        searchState = newSearchState;
        if (searchState == STATE_SEARCH_SONG) {
            setChecked(layoutBinding.btnSearchSong);
            unChecked(layoutBinding.btnSearchSinger);
            createSongRecycleView();
        } else {
            setChecked(layoutBinding.btnSearchSinger);
            unChecked(layoutBinding.btnSearchSong);
            createSingerRecycleView();
        }
    }

    void setChecked(MaterialButton button) {
        button.setStrokeColor(ContextCompat.getColorStateList(this.getContext(), R.color.green));
        button.setTextColor(ContextCompat.getColorStateList(this.getContext(), R.color.green));
    }

    void unChecked(MaterialButton button) {
        button.setStrokeColor(ContextCompat.getColorStateList(this.getContext(), R.color.white));
        button.setTextColor(ContextCompat.getColorStateList(this.getContext(), R.color.white));
    }

    void createSongRecycleView() {
        List<Song> songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, (ReplaceFragmentListener) getContext());
        layoutBinding.rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        layoutBinding.rvSearchResult.setAdapter(songAdapter);
    }

    void createSingerRecycleView() {
        List<Singer> singerList = new ArrayList<>();
        singerAdapter = new SingerAdapter(singerList, (ReplaceFragmentListener) getContext());
        layoutBinding.rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        layoutBinding.rvSearchResult.setAdapter(singerAdapter);
    }

    void createSearchView() {
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        layoutBinding.tvSearch.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        layoutBinding.tvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    return false;
                }
                if (searchState == STATE_SEARCH_SONG) {
                    searchPresenter.searchSong(query.trim());
                } else {
                    searchPresenter.searchSinger(query.trim());
                }
                loadListener.onLoad();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    return false;
                }
                if (searchState == STATE_SEARCH_SONG) {
                    searchPresenter.searchSong(newText.trim());
                } else {
                    searchPresenter.searchSinger(newText.trim());
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        loadListener = (LoadListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        replaceFragmentListener.showComponents();
        initEvent();
        createSearchView();
        applySearchState(STATE_SEARCH_SONG);
        super.onViewCreated(view, savedInstanceState);
    }

    void initEvent() {
        layoutBinding.btnSearchSinger.setOnClickListener(v -> applySearchState(STATE_SEARCH_SINGER));
        layoutBinding.btnSearchSong.setOnClickListener(v -> applySearchState(STATE_SEARCH_SONG));
    }

    @Override
    public void onSearching() {

    }

    @Override
    public void onSongResponse(List<Song> songList) {
        if (songList != null) {
            songAdapter.updateData(songList);
            layoutBinding.tvNoResult.setVisibility(View.GONE);
        } else {
            songAdapter.updateData(null);
            layoutBinding.tvNoResult.setVisibility(View.VISIBLE);
        }
        completeSearch();
    }

    @Override
    public void onSingerResponse(List<Singer> singerList) {
        if (singerList != null) {
            singerAdapter.updateData(singerList);
            layoutBinding.tvNoResult.setVisibility(View.GONE);
        } else {
            songAdapter.updateData(null);
            singerAdapter.notifyDataSetChanged();
            layoutBinding.tvNoResult.setVisibility(View.VISIBLE);
        }
        completeSearch();
    }

    void completeSearch() {
        layoutBinding.anmSearch.setVisibility(View.GONE);
        loadListener.onComplete();
    }
}