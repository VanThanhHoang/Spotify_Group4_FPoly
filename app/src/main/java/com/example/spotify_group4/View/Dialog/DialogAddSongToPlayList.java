package com.example.spotify_group4.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Listener.UserPlayListListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Presenter.UserPlayListFragmentPresenter;
import com.example.spotify_group4.databinding.AddSongToPlaylistDialogBinding;

import java.util.List;

public class DialogAddSongToPlayList extends Dialog implements UserPlayListListener {
    UserPlayListFragmentPresenter userPlayListFragmentPresenter;
    AddSongToPlaylistDialogBinding addPlaylistDialogBinding;
    int playListId = -1;
    String userId;
    String[] playlistNames;
    List<PlayList> playLists;
    int songId;

    public DialogAddSongToPlayList(@NonNull Context context, String userId, int songId) {
        super(context);
        this.userId = userId;
        this.songId = songId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addPlaylistDialogBinding = AddSongToPlaylistDialogBinding.inflate(getLayoutInflater(), null, false);
        setContentView(addPlaylistDialogBinding.getRoot());
        customWindow();
        userPlayListFragmentPresenter = new UserPlayListFragmentPresenter(getContext(), this);
        userPlayListFragmentPresenter.getUserPlayList(userId);
        addPlaylistDialogBinding.btnAddPlayList.setOnClickListener(v -> {
            if (playListId != -1) {
               if(userPlayListFragmentPresenter.addSongToPlayList(playListId, songId)){
                   dismiss();
               }
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn playlist", Toast.LENGTH_SHORT).show();
            }
        });
        super.onCreate(savedInstanceState);
    }

    void customWindow() {
        this.setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    void fillDataToDropDownMenu(List<PlayList> playLists) {
        playlistNames = new String[playLists.size()];
        for (int i = 0; i < playLists.size(); i++) {
            playlistNames[i] = playLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, playlistNames);
        addPlaylistDialogBinding.atcPlaylist.setAdapter(adapter);
        addPlaylistDialogBinding.atcPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playListId = playLists.get(position).getId();
                addPlaylistDialogBinding.atcPlaylist.setText(playlistNames[position], false);
            }
        });
    }

    @Override
    public void onGetListPlayListComplete(List<PlayList> playLists) {
        this.playLists = playLists;
        fillDataToDropDownMenu(this.playLists);
    }

    @Override
    public void onUpdatePlaylist() {

    }
}
