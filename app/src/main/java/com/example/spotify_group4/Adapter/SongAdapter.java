package com.example.spotify_group4.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.MusicPlayFragment;
import com.example.spotify_group4.databinding.SongItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songViewHolder> {
    SongItemBinding songItemBinding;
    List<Song> songList;
    ReplaceFragmentListener replaceFragmentListener;

    public SongAdapter(List<Song> songList, ReplaceFragmentListener replaceFragmentListener) {
        this.songList = songList;
        this.replaceFragmentListener = replaceFragmentListener;
    }


    @NonNull
    @Override
    public songViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        songItemBinding = SongItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new songViewHolder(songItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull songViewHolder holder, int position) {
        int positionItem = holder.getLayoutPosition();
        Song song = songList.get(positionItem);
        holder.cardViewContainer.setOnClickListener(v -> replaceFragmentListener
                .replaceFragment(
                        new MusicPlayFragment(songList, positionItem)
                )
        );
        Picasso.get().load(song.getUrlImg()).placeholder(R.drawable.img_load).into(holder.imgSong);
        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());
    }

    public void updateData(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(songList!=null){
            return songList.size();
        }
        return 0;
    }

    public static class songViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewContainer;
        TextView tvSongName, tvSingerName;
        ImageView imgSong;
        public songViewHolder(@NonNull SongItemBinding songItemBinding) {
            super(songItemBinding.getRoot());
            cardViewContainer = songItemBinding.container;
            tvSingerName = songItemBinding.tvSinger;
            tvSongName = songItemBinding.tvSongName;
            imgSong = songItemBinding.imgSinger;
        }
    }
}
