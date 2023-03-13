package com.example.spotify_group4.Helper;

import android.annotation.SuppressLint;

public class TimeFormatter {
    @SuppressLint("DefaultLocale")
    public static String formatMillisecondToMinuteAndSecond(int duration) {
        int minutes = (duration / 1000) / 60; // tính số phút
        int seconds = (duration / 1000) % 60; // tính số giây
        return String.format("%02d:%02d", minutes, seconds);
    }
}
