package com.example.spotify_group4.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.View.Fragment.PlayListFragment;
import com.example.spotify_group4.databinding.PlaylistItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    PlaylistItemBinding playlistItemBinding;
    List<PlayList> playLists;
    ReplaceFragmentListener replaceFragmentListener;

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
        holder.cardViewContain.setOnClickListener(v -> replaceFragmentListener.replaceFragment(new PlayListFragment()));
        PlayList playList = playLists.get(positionItem);
        Picasso.get().load(playList.getUrlImg()).into(holder.imgPlayList);
        holder.tvPlaylistName.setText(playList.getName());
    }


    @Override
    public int getItemCount() {
        return playLists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaylistName;
        ImageView imgPlayList;
        CardView cardViewContain;

        public PlaylistViewHolder(@NonNull PlaylistItemBinding playlistItemBinding) {
            super(playlistItemBinding.getRoot());
            imgPlayList = playlistItemBinding.imgPlayList;
            tvPlaylistName = playlistItemBinding.tvPlayListName;
            cardViewContain = playlistItemBinding.container;
        }
    }
}
