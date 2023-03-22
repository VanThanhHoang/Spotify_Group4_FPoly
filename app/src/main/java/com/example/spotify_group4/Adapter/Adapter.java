package com.example.spotify_group4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_group4.Model.User;
import com.example.spotify_group4.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.UserViewHolder>{

    private Context mContext;
    private List<User> mListUser;

    public Adapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<User> list){
        this.mListUser = list;
        notifyDataSetChanged();
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
        if(user == null){
            return;
        }
        holder.imageUser.setImageResource(user.getResourceId());
        holder.tvName.setText(user.getName());
        holder.imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, user.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
        }


    public class UserViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageUser;
        private TextView tvName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser= itemView.findViewById(R.id.imageView);
            tvName= itemView.findViewById(R.id.textView);
        }
    }
}
