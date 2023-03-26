package com.example.spotify_group4.View.Fragment;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.databinding.LayoutHomeSliderBinding;
import com.squareup.picasso.Picasso;

public class HomeSliderFragment extends Fragment {
    PlayList playList;
    LayoutHomeSliderBinding layoutHomeSliderBinding;
    ReplaceFragmentListener mReplaceFragmentListener;

    public HomeSliderFragment(PlayList playList, ReplaceFragmentListener replaceFragmentListener) {
        mReplaceFragmentListener = replaceFragmentListener;
        this.playList = playList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutHomeSliderBinding = LayoutHomeSliderBinding.inflate(getLayoutInflater(), container, false);
        return layoutHomeSliderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setOnClickListener(v ->
                mReplaceFragmentListener.replaceFragment(new PlayListFragment(playList))
        );
        layoutHomeSliderBinding.tvPlayListName.setText(playList.getName());
        Picasso.get().load(playList.getUrlImg()).into(layoutHomeSliderBinding.imgSlider);
        setColorMatrix(layoutHomeSliderBinding.imgSlider);
        super.onViewCreated(view, savedInstanceState);
    }

    void setColorMatrix(ImageView imgPlayList) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // 0 means grayscale
        colorMatrix.setScale(0.8f, 0.8f, 0.7f, 1); // Scale all color channels by 0.5
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imgPlayList.setColorFilter(colorFilter);
    }
}
