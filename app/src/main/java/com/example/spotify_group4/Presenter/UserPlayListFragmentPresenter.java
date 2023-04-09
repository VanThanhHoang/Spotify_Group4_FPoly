package com.example.spotify_group4.Presenter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Listener.UserPlayListListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.databinding.AddPlaylistDialogBinding;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPlayListFragmentPresenter {
    Context context;
    UserPlayListListener userPlayListListener;

    public UserPlayListFragmentPresenter(Context context, UserPlayListListener userPlayListListener) {
        this.context = context;
        this.userPlayListListener = userPlayListListener;
    }

    public boolean addSongToPlayList(int playListId, int songId) {
        final boolean[] isComplete = {false};
        Call<String> insertSongToUserPlayList = ApiSkyMusic.apiSkyMusic.add_song_to_play_list(playListId, songId);
        insertSongToUserPlayList.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    if (!response.body().equals("noComplete")) {
                        isComplete[0] = true;
                        Toast.makeText(context, "Thêm thành công vào playlist", Toast.LENGTH_SHORT).show();
                    } else {
                        isComplete[0] = false;
                        Toast.makeText(context, "Bài hát đã tồn tại trong playlist", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        return isComplete[0];
    }

    public void getUserPlayList(String userId) {
        Call<List<PlayList>> callGetUSerPlay = ApiSkyMusic.apiSkyMusic.getUserPlayList(userId);
        callGetUSerPlay.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlayList>> call, @NonNull Response<List<PlayList>> response) {
                userPlayListListener.onGetListPlayListComplete(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<List<PlayList>> call, @NonNull Throwable t) {
            }
        });
    }

    public void addUserPlayList(String userId, String playListName) {
        Call<Void> getUserPlayLists = ApiSkyMusic.apiSkyMusic.add_play_list(userId, playListName);
        getUserPlayLists.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }

    public void deleteUserPlayList(int playlistId) {
        Call<Void> getUserPlayLists = ApiSkyMusic.apiSkyMusic.delete_play_list(playlistId);
        getUserPlayLists.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }

    public void showDialogUpdatePlayList(PlayList playList) {
        Dialog dialogUpdatePlaylist = new Dialog(context);
        AddPlaylistDialogBinding addPlaylistDialogBinding = AddPlaylistDialogBinding.inflate(LayoutInflater.from(context), null, false);
        dialogUpdatePlaylist.setContentView(addPlaylistDialogBinding.getRoot());
        addPlaylistDialogBinding.tvTittle.setText("Cập nhật tên playlist");
        addPlaylistDialogBinding.btnAddPlayList.setText("Cập nhật");
        addPlaylistDialogBinding.edPlayListName.setText(playList.getName());
        Window window = dialogUpdatePlaylist.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        addPlaylistDialogBinding.btnAddPlayList.setOnClickListener(v -> {
            playList.setName(Objects.requireNonNull(addPlaylistDialogBinding.edPlayListName.getText()).toString());
            Log.d("123", "showDialogUpdatePlayList: " + playList.getName());
            Call<Void> callUpdatePlayList = ApiSkyMusic.apiSkyMusic.update_play_list(playList.getId(), playList.getName());
            dialogUpdatePlaylist.dismiss();
            callUpdatePlayList.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    userPlayListListener.onUpdatePlaylist();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                }
            });
        });
        dialogUpdatePlaylist.show();
    }
}
