package com.example.spotify_group4.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Listener.AccountFragmentItemListener;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Activity.LoginActivity;
import com.example.spotify_group4.View.Activity.MainActivity;

public class MenuAccountFragmentAdapter extends RecyclerView.Adapter<MenuAccountFragmentAdapter.UserViewHolder> {
    private static final String[] MENU_ITEM = new String[]{
            "Tài khoản",
            "Tải nhạc lên",
            "Thiết bị",
            "Nội dung nhạy cảm",
            "Quyền riêng tư",
            "Chất lượng âm thanh",
            "Không gian lưu trữ",
            "Thông báo",
            "Quảng cáo",
            "Đăng xuất"
    };
    AccountFragmentItemListener accountFragmentItemListener;

    public MenuAccountFragmentAdapter(AccountFragmentItemListener accountFragmentItemListener) {
        this.accountFragmentItemListener = accountFragmentItemListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_account, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.btnItem.setText(MENU_ITEM[position]);
        if(position != 1 && position != MENU_ITEM.length - 1) {
            holder.btnItem.setOnClickListener(v -> Toast.makeText(v.getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());
        }
        if(position == 1) {
            holder.btnItem.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            });
        }
        if (position == MENU_ITEM.length - 1) {
            holder.btnItem.setOnClickListener(v -> {
//                SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPref", 0);
//                String userId = sharedPreferences.getString("token", "");
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("MySharedPref", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
              v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
            });
        }
    }

    @Override
    public int getItemCount() {
        return MENU_ITEM.length;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final Button btnItem;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            btnItem = itemView.findViewById(R.id.btnItemMenuAccountFragment);
        }
    }
}
