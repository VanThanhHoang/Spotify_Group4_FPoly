package com.example.spotify_group4.View.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class HomeFragment extends Fragment {
    FragmentHomeBinding layoutBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /*loadMusic();*/
        super.onViewCreated(view, savedInstanceState);
    }

    void loadMusic(){
        String url2 ="https://cf-media.sndcdn.com/0ZmbpsZb78fk.128.mp3?Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiKjovL2NmLW1lZGlhLnNuZGNkbi5jb20vMFptYnBzWmI3OGZrLjEyOC5tcDMqIiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNjc4MjkwMzM4fX19XX0_&Signature=Jvv66HRuCcYICo~NK8WOJUrvx3Bu~Go9Yl6dl3e1Sftef82VtKqm1BqFS5TDBmShvpz3FANz37~7~s49UcO10zJhyi5W2JyPHMABN4NE1AAH4Dnyl5Rd0uibZmr27-A9nbHnP6mou8r00fEFZXaMG6RYXLrVdJayhu2BZYu7l9iqmHRHRgped3SL3dgHrXxVd00w7s3hN740m6bq3sC9CWVzzRwMNh5tQYHm3eS4sVbwS7Zw53RE9XxWZDs~gaIdbQIe8GKMFSL6Ggfc5F24OTuV7f5aZhfiQSAMKoy1rKfqCh65uhl12X~eRYuHE0-cLNs91AjsSaUgOFrEQjhaaQ__&Key-Pair-Id=APKAI6TU7MMXM5DG6EPQ";
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url2);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
