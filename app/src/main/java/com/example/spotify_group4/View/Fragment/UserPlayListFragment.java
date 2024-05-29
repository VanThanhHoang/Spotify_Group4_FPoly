package com.example.spotify_group4.View.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotify_group4.Adapter.UserPlaylistAdapter;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Listener.UserPlayListListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Presenter.UserPlayListFragmentPresenter;
import com.example.spotify_group4.View.Dialog.DialogAddPlayList;
import com.example.spotify_group4.databinding.FragmentUserPlaylistBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class UserPlayListFragment extends Fragment implements UserPlayListListener {
    FragmentUserPlaylistBinding layoutBinding;
    UserPlayListFragmentPresenter userPlayListFragmentPresenter;
    String userId;
    DialogAddPlayList dialogAddPlayList;
    UserPlaylistAdapter userPlaylistAdapter ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentUserPlaylistBinding.inflate(getLayoutInflater(), null, false);
        getUserId();
        if(getContext()!=null){
            dialogAddPlayList = new DialogAddPlayList(getContext(), userId,this);
        }
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userPlayListFragmentPresenter = new UserPlayListFragmentPresenter(getContext(), this);
        userPlayListFragmentPresenter.getUserPlayList(userId);
        createRv();
        setEvent();
        super.onViewCreated(view, savedInstanceState);
    }
    void createRv(){
        userPlaylistAdapter = new UserPlaylistAdapter((ReplaceFragmentListener) getContext(),userPlayListFragmentPresenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        layoutBinding.rvUserPlayList.setAdapter(userPlaylistAdapter);
        layoutBinding.rvUserPlayList.setLayoutManager(linearLayoutManager);
    }
    void setEvent() {
        layoutBinding.btnAddPlayList.setOnClickListener(v -> dialogAddPlayList.show());
    }

    void getUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", 0);
        userId = sharedPreferences.getString("token", "");
    }

    @Override
    public void onGetListPlayListComplete(List<PlayList> playLists) {
        userPlaylistAdapter.updateData(playLists);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdatePlaylist() {
     createRv();
    }
}
