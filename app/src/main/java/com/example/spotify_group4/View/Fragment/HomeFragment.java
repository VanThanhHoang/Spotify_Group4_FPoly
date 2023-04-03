package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.HomeSliderAdapter;
import com.example.spotify_group4.Adapter.PlaylistAdapter;
import com.example.spotify_group4.Helper.AnimationZoomViewPager;
import com.example.spotify_group4.Listener.GetDataHomeFragmentListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Presenter.HomeFragmentPresenter;
import com.example.spotify_group4.databinding.FragmentHomeBinding;

import java.time.LocalTime;
import java.util.List;


public class HomeFragment extends Fragment implements GetDataHomeFragmentListener {
    FragmentHomeBinding layoutBinding;
    HomeFragmentPresenter homeFragmentPresenter;
    RecyclerView[] listDefaultPlayList;
    TextView[] listDefaultContentTittle;
    ReplaceFragmentListener replaceFragmentListener;
    Handler handler;
    Runnable sliderRunnable;
    List<HomeContent> mHomeContents;
    List<PlayList> mPlayListSlider;
    boolean isCreateHomeContent;
    boolean isCreatePlaylistSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        homeFragmentPresenter = new HomeFragmentPresenter(this);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setTextHello();
        initComponent();
        if (!isCreateHomeContent) {
            homeFragmentPresenter.getHomeContent();
            layoutBinding.shimmerLayout.startShimmer();
        } else {
            hideShimmer();
            createTittleContent(mHomeContents);
            createRecycleViewPlaylist(mHomeContents);
        }
        if (!isCreatePlaylistSlider) {
            homeFragmentPresenter.getPlayListForSlider();
        } else {
            createSlider(mPlayListSlider);
        }
        initEvent();

        super.onViewCreated(view, savedInstanceState);
    }

    void initComponent() {
        listDefaultPlayList = new RecyclerView[]{
                layoutBinding.rvPlayListDefault0,
                layoutBinding.rvPlayListDefault1,
                layoutBinding.rvPlayListDefault2,
                layoutBinding.rvPlayListDefault3,
                layoutBinding.rvPlayListDefault4,
                layoutBinding.rvPlayListDefault5,
                layoutBinding.rvPlayListDefault6,
                layoutBinding.rvPlayListDefault7
        };
        listDefaultContentTittle = new TextView[]{
                layoutBinding.tvContentTittle0,
                layoutBinding.tvContentTittle1,
                layoutBinding.tvContentTittle2,
                layoutBinding.tvContentTittle3,
                layoutBinding.tvContentTittle4,
                layoutBinding.tvContentTittle5,
                layoutBinding.tvContentTittle6,
                layoutBinding.tvContentTittle7
        };
        replaceFragmentListener.showComponents();
    }

    void hideShimmer() {
        layoutBinding.shimmerLayout.hideShimmer();
        if (layoutBinding.layoutRefresh.isRefreshing()) {
            layoutBinding.layoutRefresh.setRefreshing(false);
        }
        layoutBinding.shimmerLayout.setVisibility(View.INVISIBLE);
    }

    void createSlider(List<PlayList> playLists) {
        if (getActivity() != null) {
            HomeSliderAdapter homeSliderAdapter = new HomeSliderAdapter(getActivity(), playLists, replaceFragmentListener);
            layoutBinding.vpgHomeSlider.setAdapter(homeSliderAdapter);
            layoutBinding.vpgHomeSlider.setPageTransformer(new AnimationZoomViewPager());
            layoutBinding.homeCircleIndicatior.setViewPager(layoutBinding.vpgHomeSlider);
            homeSliderAdapter.registerAdapterDataObserver(layoutBinding.homeCircleIndicatior.getAdapterDataObserver());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        replaceFragmentListener = (ReplaceFragmentListener) getContext();
    }


    void createTittleContent(List<HomeContent> homeContents) {
        for (int i = 0; i < listDefaultContentTittle.length; i++) {
            listDefaultContentTittle[i].setText(homeContents.get(i).getName());
        }
    }

    void createRecycleViewPlaylist(List<HomeContent> homeContents) {
        for (int i = 0; i < listDefaultContentTittle.length; i++) {
            PlaylistAdapter adapter = new PlaylistAdapter(homeContents.get(i).getPlayLists(), replaceFragmentListener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            listDefaultPlayList[i].setLayoutManager(linearLayoutManager);
            listDefaultPlayList[i].setAdapter(adapter);
        }
    }

    void initEvent() {
        layoutBinding.layoutRefresh.setOnRefreshListener(() -> {
            homeFragmentPresenter.getPlayListForSlider();
            homeFragmentPresenter.getHomeContent();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (layoutBinding.layoutRefresh.isRefreshing()) {
                    layoutBinding.layoutRefresh.setRefreshing(false);
                }
            }, 2000);
        });
        layoutBinding.layoutScroll.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                replaceFragmentListener.hideBotNav();
            } else {
                replaceFragmentListener.showBotNav();
            }

        });
    }

    private void autoTransSlider(int sizePlayList) {
        handler = new Handler(Looper.getMainLooper());
        sliderRunnable = () -> {
            int currentPos = layoutBinding.vpgHomeSlider.getCurrentItem();
            if (currentPos < sizePlayList) {
                layoutBinding.vpgHomeSlider.setCurrentItem(++currentPos);
            } else {
                layoutBinding.vpgHomeSlider.setCurrentItem(0);
            }
            handler.postDelayed(sliderRunnable, 3000);
        };
        sliderRunnable.run();
    }

    void setTextHello() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        if (hour >= 4 && hour < 12) {
            layoutBinding.tvHelloHomeFragment.setText("Chào buổi sáng!");
        } else if (hour >= 12 && hour < 15) {
            layoutBinding.tvHelloHomeFragment.setText("Buổi trưa vui vẻ nhé !");
        } else if (hour >= 15 && hour < 18) {
            layoutBinding.tvHelloHomeFragment.setText("Chúc buổi chiều đầy năng động!");
        } else {
            layoutBinding.tvHelloHomeFragment.setText("Buổi tối tốt lành !");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (handler != null) {
            handler.removeCallbacks(sliderRunnable);
        }
        super.onStop();
    }

    @Override
    public void onGetListPlayListComplete(List<PlayList> playLists) {
        mPlayListSlider = playLists;
        isCreatePlaylistSlider = true;
        createSlider(playLists);
        autoTransSlider(playLists.size() - 1);
    }

    @Override
    public void onGetListPlayListFail() {

    }

    @Override
    public void onGetHomeContentComplete(List<HomeContent> homeContents) {
        mHomeContents = homeContents;
        isCreateHomeContent = true;
        createTittleContent(homeContents);
        createRecycleViewPlaylist(homeContents);
        hideShimmer();
    }

    @Override
    public void onGetHomeContentFail() {

    }
}
