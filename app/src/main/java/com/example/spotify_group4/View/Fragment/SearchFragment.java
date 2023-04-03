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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotify_group4.Adapter.SongAdapter;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Listener.SearchListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.SearchPresenter;
import com.example.spotify_group4.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchListener {
    FragmentSearchBinding layoutBinding;
    SearchPresenter searchPresenter;
    SongAdapter songAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        searchPresenter = new SearchPresenter(this);
        return layoutBinding.getRoot();
    }

    void createRecycleView() {
        List<Song> songList = new ArrayList<>();
        songAdapter = new SongAdapter(songList, (ReplaceFragmentListener) getContext());
        layoutBinding.rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        layoutBinding.rvSearchResult.setAdapter(songAdapter);
    }

    void createSearchView() {
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        layoutBinding.tvSearch.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        layoutBinding.tvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPresenter.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPresenter.search(newText);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createSearchView();
        createRecycleView();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSearching() {

    }

    @Override
    public void onResponse(List<Song> songList) {
        songAdapter.updateData(songList);
    }
}