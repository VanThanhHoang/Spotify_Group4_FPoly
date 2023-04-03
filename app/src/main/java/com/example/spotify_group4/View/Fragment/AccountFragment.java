package com.example.spotify_group4.View.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.MenuAccountFragmentAdapter;
import com.example.spotify_group4.Listener.AccountFragmentItemListener;
import com.example.spotify_group4.Presenter.AccountFragmentPresenter;
import com.example.spotify_group4.databinding.FragmentAcountBinding;

public class AccountFragment extends Fragment implements AccountFragmentItemListener {
    FragmentAcountBinding layoutBinding;
    AccountFragmentPresenter accountFragmentPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentAcountBinding.inflate(getLayoutInflater(), container, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        accountFragmentPresenter = new AccountFragmentPresenter(getActivity());
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createRecycleView();
    }
    void createRecycleView(){
        MenuAccountFragmentAdapter menuAccountFragmentAdapter = new MenuAccountFragmentAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutBinding.rcvMenu.setLayoutManager(linearLayoutManager);
        layoutBinding.rcvMenu.setAdapter(menuAccountFragmentAdapter);
    }

    @Override
    public boolean signOut() {
        return accountFragmentPresenter.signOut();
    }
}