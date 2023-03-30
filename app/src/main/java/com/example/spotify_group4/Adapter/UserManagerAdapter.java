package com.example.spotify_group4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Activity.MainActivity;

import java.util.List;

public class UserManagerAdapter extends RecyclerView.Adapter<UserManagerAdapter.UserViewHolder> {
    private List<User> mListUser; 
    Context context;

    public UserManagerAdapter(Context context) {
        this.context=context;
    }

    public void setData(List<User> list) {
        this.mListUser = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {
            return;
        }
        holder.imageUser.setImageResource(user.getResourceId());
        holder.tvName.setText(user.getName());
        holder.imageUser.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("data", user.getName());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUser;
        private TextView tvName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.textView);
        }
    }
}
