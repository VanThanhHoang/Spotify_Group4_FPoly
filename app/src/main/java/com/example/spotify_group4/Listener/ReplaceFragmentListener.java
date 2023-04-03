package com.example.spotify_group4.Listener;

import androidx.fragment.app.Fragment;

public interface ReplaceFragmentListener {
    void replaceFragment(Fragment fragment);
    void hideComponents();
    void showComponents();
    void hideBotNav();
    void showBotNav();
}
