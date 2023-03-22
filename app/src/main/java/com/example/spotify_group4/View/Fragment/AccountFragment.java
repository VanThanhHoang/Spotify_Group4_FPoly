package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Adapter.Adapter;
import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.R;
import com.example.spotify_group4.databinding.FragmentAcountBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    FragmentAcountBinding layoutBinding ;
    private RecyclerView rcv;
    private Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentAcountBinding.inflate(getLayoutInflater(),container,false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv = view.findViewById(R.id.rcvName);
        adapter = new Adapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv.setLayoutManager(linearLayoutManager);
        adapter.setData(getListUser());
        rcv.setAdapter(adapter);
    }
    private List<User> getListUser(){
        List<User> list = new ArrayList<>();
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Tài khoản"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Trình tiết kiệm dữ liệu"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Ngôn ngữ"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Phát nhạc"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Nội dung nhạy cảm"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Thiết bị"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Ô tô"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Quyền riêng tư và mạng xã hội"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Trợ lý thoại và ứng dụng"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Chất lượng âm thanh"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Chất lượng video"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Không gian lưu trữ"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Thông báo"));
        list.add(new User(R.drawable.ic_baseline_navigate_next_24,"Quảng cáo"));
        return list;
    }
}