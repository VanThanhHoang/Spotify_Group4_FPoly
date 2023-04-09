package com.example.spotify_group4.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Listener.UserPlayListListener;
import com.example.spotify_group4.Presenter.UserPlayListFragmentPresenter;
import com.example.spotify_group4.databinding.AddPlaylistDialogBinding;

import java.util.Objects;

public class DialogAddPlayList extends Dialog {
    UserPlayListFragmentPresenter userPlayListFragmentPresenter;
    AddPlaylistDialogBinding addPlaylistDialogBinding;
    UserPlayListListener userPlayListListener;
    String userId;

    public DialogAddPlayList(@NonNull Context context,String userId,UserPlayListListener userPlayListListener) {
        super(context);
        this.userId = userId;
        this.userPlayListListener = userPlayListListener;
        this.userPlayListFragmentPresenter = new UserPlayListFragmentPresenter(context, userPlayListListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addPlaylistDialogBinding = AddPlaylistDialogBinding.inflate(getLayoutInflater(), null, false);
        setContentView(addPlaylistDialogBinding.getRoot());
        customWindow();
        userPlayListFragmentPresenter.getUserPlayList(userId);
        super.onCreate(savedInstanceState);
    }

    void customWindow() {
        this.setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addPlaylistDialogBinding.btnAddPlayList.setOnClickListener(v -> {
            if (Objects.requireNonNull(addPlaylistDialogBinding.edPlayListName.getText()).length() == 0) {
                addPlaylistDialogBinding.tilPlaylist.setError("Nhập tên Playlist");
            } else {
                userPlayListFragmentPresenter.addUserPlayList(userId, addPlaylistDialogBinding.edPlayListName.getText().toString());
                userPlayListFragmentPresenter.getUserPlayList(userId);
                dismiss();
            }
        });
    }
}
