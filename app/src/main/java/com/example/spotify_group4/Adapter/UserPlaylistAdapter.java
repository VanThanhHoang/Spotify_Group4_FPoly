package com.example.spotify_group4.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Presenter.UserPlayListFragmentPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.PlayListFragment;
import com.example.spotify_group4.databinding.UserPlaylistItemBinding;

import java.util.List;

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.PlaylistViewHolder> {
    UserPlaylistItemBinding playlistItemBinding;
    List<PlayList> playLists;
    ReplaceFragmentListener replaceFragmentListener;
    UserPlayListFragmentPresenter userPlayListFragmentPresenter;

    public UserPlaylistAdapter(ReplaceFragmentListener replaceFragmentListener, UserPlayListFragmentPresenter userPlayListFragmentPresenter) {
        this.replaceFragmentListener = replaceFragmentListener;
        this.userPlayListFragmentPresenter = userPlayListFragmentPresenter;
    }

    public void updateData(List<PlayList> playLists) {
        this.playLists = playLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        playlistItemBinding = UserPlaylistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistViewHolder(playlistItemBinding);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int positionItem = holder.getLayoutPosition();
        PlayList playList = playLists.get(positionItem);
        int viewPos= holder.getLayoutPosition();
        holder.cardViewContain.setOnClickListener(v -> {
            replaceFragmentListener.replaceFragment(new PlayListFragment(playList,PlayListFragment.FLAG_USER_PLAYLIST));
            replaceFragmentListener.hideComponents();
        });
        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.btnMenu.getContext(), holder.btnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.context_menu_add_edit, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.itemDelete:
                        notifyItemRemoved(viewPos);
                        playLists.remove(viewPos);
                        userPlayListFragmentPresenter.deleteUserPlayList(playList.getId());
                        return true;
                    case R.id.itemUpdate:
                        userPlayListFragmentPresenter.showDialogUpdatePlayList(playList);
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });
        holder.tvPlaylistName.setText(playList.getName());
    }

    @Override
    public int getItemCount() {
        if (playLists == null) {
            return 0;
        }
        return playLists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaylistName;
        CardView cardViewContain;
        ImageButton btnMenu;

        public PlaylistViewHolder(@NonNull UserPlaylistItemBinding playlistItemBinding) {
            super(playlistItemBinding.getRoot());
            tvPlaylistName = playlistItemBinding.tvLibName;
            btnMenu = playlistItemBinding.btnMenu;
            cardViewContain = playlistItemBinding.container;
        }
    }

}
