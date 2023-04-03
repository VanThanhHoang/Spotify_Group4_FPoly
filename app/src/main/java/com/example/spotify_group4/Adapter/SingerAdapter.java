package com.example.spotify_group4.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.PlayListFragment;
import com.example.spotify_group4.databinding.SingerItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.singerViewHolder> {
    SingerItemBinding singerItemBinding;
    List<Singer> singerList;
    ReplaceFragmentListener replaceFragmentListener;

    public SingerAdapter(List<Singer> singerList, ReplaceFragmentListener replaceFragmentListener) {
        this.singerList= singerList;
        this.replaceFragmentListener = replaceFragmentListener;
    }


    @NonNull
    @Override
    public SingerAdapter.singerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        singerItemBinding = SingerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SingerAdapter.singerViewHolder(singerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull singerViewHolder holder, int position) {
        int positionItem = holder.getLayoutPosition();
        Singer singer =singerList.get(positionItem);
        holder.cardViewContainer.setOnClickListener(v -> replaceFragmentListener
                .replaceFragment(new PlayListFragment(singer))
        );
        Picasso.get().load(singer.getUrlImg()).placeholder(R.drawable.img_load).into(holder.imgSinger);
        holder.tvSingerName.setText(singer.getName());
    }

    public void updateData(List<Singer> singerList) {
        this.singerList = singerList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return singerList.size();
    }

    public static class singerViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewContainer;
        TextView tvSingerName;
        ImageView imgSinger;

        public singerViewHolder(@NonNull SingerItemBinding singerItemBinding) {
            super(singerItemBinding.getRoot());
            cardViewContainer = singerItemBinding.container;
            tvSingerName = singerItemBinding.tvSingerName;
            imgSinger = singerItemBinding.imgSinger;
        }
    }
}
