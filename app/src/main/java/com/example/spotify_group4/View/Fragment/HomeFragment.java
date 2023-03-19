package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.PlaylistAdapter;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    FragmentHomeBinding layoutBinding;
    List<PlayList> playLists;
    RecyclerView[] listDefaultPlayList;
    TextView[] listDefaultContentTittle;
    ReplaceFragmentListener replaceFragmentListener;
    List<HomeContent> homeContents;

    void createPlayListRecycleView() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return layoutBinding.getRoot();
    }


    void createPlayLists() {
        playLists = new ArrayList<>();
        Call<List<PlayList>> callGetPlayList = ApiSkyMusic.apiSkyMusic.getAllPlayList();
        callGetPlayList.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlayList>> call, @NonNull Response<List<PlayList>> response) {
                layoutBinding.shimmerLayout.hideShimmer();
                if (layoutBinding.layoutRefresh.isRefreshing()) {
                    layoutBinding.layoutRefresh.setRefreshing(false);
                }
                layoutBinding.shimmerLayout.setVisibility(View.INVISIBLE);
                playLists = response.body();
                createPlayListRecycleView();
            }

            @Override
            public void onFailure(@NonNull Call<List<PlayList>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Sever bận !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getHomeContent() {
        homeContents = new ArrayList<>();
        Call<List<HomeContent>> callGetHomeContent = ApiSkyMusic.apiSkyMusic.getHomeContent();
        callGetHomeContent.enqueue(new Callback<List<HomeContent>>() {
            @Override
            public void onResponse(@NonNull Call<List<HomeContent>> call, @NonNull Response<List<HomeContent>> response) {
                homeContents = response.body();
                createTittleContent();
                createRecycleViewPlaylist();
            }

            @Override
            public void onFailure(@NonNull Call<List<HomeContent>> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        replaceFragmentListener = (ReplaceFragmentListener) getContext();
    }

    void createRecycleView() {

    }
    void createTittleContent(){
        for(int i = 0;i<listDefaultContentTittle.length;i++){
            listDefaultContentTittle[i].setText(homeContents.get(i).getName());
        }
    }
    void createRecycleViewPlaylist(){
        for(int i = 0;i<listDefaultContentTittle.length;i++){
            PlaylistAdapter adapter = new PlaylistAdapter(homeContents.get(i).getPlayLists(),replaceFragmentListener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            listDefaultPlayList[i].setLayoutManager(linearLayoutManager);
            listDefaultPlayList[i].setAdapter(adapter);
        }
    }
    void initEvent() {
        layoutBinding.layoutRefresh.setOnRefreshListener(() -> {
            createPlayLists();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (layoutBinding.layoutRefresh.isRefreshing()) {
                    layoutBinding.layoutRefresh.setRefreshing(false);
                }
            }, 2000);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutBinding.shimmerLayout.startShimmer();
        createPlayLists();
        getHomeContent();
        initEvent();
        listDefaultPlayList = new RecyclerView[]{
                layoutBinding.rvPlayListDefault0,
                layoutBinding.rvPlayListDefault1,
                layoutBinding.rvPlayListDefault2,
                layoutBinding.rvPlayListDefault3,
                layoutBinding.rvPlayListDefault4,
                layoutBinding.rvPlayListDefault5
        };
        listDefaultContentTittle = new TextView[]{
                layoutBinding.tvContentTittle0,
                layoutBinding.tvContentTittle1,
                layoutBinding.tvContentTittle2,
                layoutBinding.tvContentTittle3,
                layoutBinding.tvContentTittle4,
                layoutBinding.tvContentTittle5
        };
        createRecycleView();
        replaceFragmentListener.showComponents();
        super.onViewCreated(view, savedInstanceState);
    }
}
