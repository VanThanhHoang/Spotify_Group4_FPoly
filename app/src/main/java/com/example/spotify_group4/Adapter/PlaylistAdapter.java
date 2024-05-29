package com.example.spotify_group4.Adapter;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.PlayListFragment;
import com.example.spotify_group4.databinding.PlaylistItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    PlaylistItemBinding playlistItemBinding;
    List<PlayList> playLists;
    ReplaceFragmentListener replaceFragmentListener;
    String[] listColor = new String[]{"#ced9df", "#3e5640", "#96a689", "#7993a0", "#ced9df", "#43636b"};

    public PlaylistAdapter(List<PlayList> playLists, ReplaceFragmentListener replaceFragmentListener) {
        this.playLists = playLists;
        this.replaceFragmentListener = replaceFragmentListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        playlistItemBinding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistViewHolder(playlistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        int positionItem = holder.getLayoutPosition();
        PlayList playList = playLists.get(positionItem);
        Picasso.get().load(R.drawable.img_load).into(holder.imgPlayList);
        holder.cardViewContain.setOnClickListener(v -> {
            replaceFragmentListener.replaceFragment(new PlayListFragment(playList));
            replaceFragmentListener.hideComponents();
        });
        Picasso.get().load(playList.getUrlImg()).placeholder(R.drawable.img_load).into(holder.imgPlayList);
        holder.tvPlaylistName.setText(playList.getName());
        setColorMatrix(playlistItemBinding.imgPlayList);
        setRandomColor(holder.leftLine, holder.botLine);
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaylistName;
        ImageView imgPlayList;
        CardView cardViewContain;
        View leftLine, botLine;

        public PlaylistViewHolder(@NonNull PlaylistItemBinding playlistItemBinding) {
            super(playlistItemBinding.getRoot());
            leftLine = playlistItemBinding.leftLine;
            botLine = playlistItemBinding.botLine;
            imgPlayList = playlistItemBinding.imgPlayList;
            tvPlaylistName = playlistItemBinding.tvPlayListName;
            cardViewContain = playlistItemBinding.container;
        }
    }

    void setRandomColor(View leftLine, View botLine) {
        Random rand = new Random();
        int num = rand.nextInt(6);
        String color = listColor[num];
        leftLine.setBackgroundColor(Color.parseColor(color));
        botLine.setBackgroundColor(Color.parseColor(color));
    }

    void setColorMatrix(ImageView imgPlayList) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // 0 means grayscale
        colorMatrix.setScale(0.8f, 0.8f, 0.8f, 1); // Scale all color channels by 0.5
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imgPlayList.setColorFilter(colorFilter);
    }
}
