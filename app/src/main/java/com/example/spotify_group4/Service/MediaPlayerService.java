package com.example.spotify_group4.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Helper.TimeFormatter;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;
import com.example.spotify_group4.SharedPreferences.AppSharedPreferenceHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MediaPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private AppSharedPreferenceHelper appSharedPreferenceHelper;
    private boolean isInitPlayPlayList;
    private MediaSessionCompat mediaSession;
    private Runnable updateSeekBar;
    private NotificationCompat.Builder notificationBuilder;
    private int currentSongPosition;
    private List<Song> songListDefault;
    private List<Song> songList;
    private Song currentSong;
    private String currentRepeatMode;

    @Override
    public void onCreate() {
        appSharedPreferenceHelper = new AppSharedPreferenceHelper(this);
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
        mediaPlayer = new MediaPlayer();
        handler = new Handler(getMainLooper());
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    updateDuration();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(Constants.ACTION_MEDIA_PLAYER, 0);
        if (action == Constants.MEDIA_PLAYER_ACTION_PLAY_LIST_SONG && !isInitPlayPlayList) {
            initPlayList(intent);
            return START_NOT_STICKY;
        }
        if (action == Constants.MEDIA_PLAYER_ACTION_CHANGE_REPEAT_MODE) {
            changeRepeatMode();
            return START_NOT_STICKY;
        }
        if (action == Constants.MEDIA_PLAYER_ACTION_TRANS_SONG_VIEWPAGER) {
            currentSongPosition = intent.getIntExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, 0);
            transSongByViewPager();
            return START_NOT_STICKY;
        } else {
            handlerMediaPlayer(action, intent);
        }
        //action
        return START_NOT_STICKY;
    }

    void changeRepeatMode() {
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
    }

    void handlerMediaPlayer(int action, Intent intent) {
        if (action == Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG) {
            transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        } else if (action == Constants.MEDIA_PLAYER_ACTION_PLAY_PREV_SONG) {
            transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_PREV_SONG);
        } else if (action == Constants.MEDIA_PLAYER_ACTION_SEEK) {
            int positionToSeek = intent.getIntExtra(Constants.MEDIA_PLAYER_EXTRA_SEEK_POSITION, 0);
            mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
            seekMedia(positionToSeek);
        } else {
            handleAction(action);
        }
    }

    void initPlayList(Intent intent) {
        isInitPlayPlayList = true;
        songListDefault = intent.getParcelableArrayListExtra(Constants.MEDIA_PLAYER_EXTRA_SONG_LIST);
        songList = songListDefault;
        currentSongPosition = intent.getIntExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, 0);
        currentSong = songList.get(currentSongPosition);
        prepareMusic(currentSong.getUrl());
        new Handler(Looper.getMainLooper()).postDelayed(this::shuffledPlayList, 300);
    }

    void shuffledPlayList() {
        if (appSharedPreferenceHelper.isShuffleModeOn()) {
            songList.remove(currentSongPosition);
            Collections.shuffle(songList);
            songList.add(currentSongPosition, currentSong);
            sendListShuffled();
        } else {
            sendListShuffled();
        }
    }

    void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "MEDIA");
        mediaSession.setActive(true);
        // set info
        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSong.getSingerName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSong.getName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album Name");
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(getResources(), R.drawable.ic_facebook));
        mediaSession.setMetadata(metadataBuilder.build());
        setMediaSessionCallBack();
        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    void prepareMusic(String url) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        try {
            setupMediaPlayer(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                startUiMusic();
                mediaPlayer.start();
                initMediaSession();
                initNotification();
                changState(PlaybackStateCompat.STATE_PLAYING);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendListShuffled() {
        Intent intent = new Intent(MediaPlayerReceiver.ACTION_SHUFFLED_PLAY_LIST);
        intent.putParcelableArrayListExtra(Constants.MEDIA_PLAYER_EXTRA_LIST_SHUFFLED,
                (ArrayList<? extends Parcelable>) songList);
        sendBroadcast(intent);
    }

    void startUiMusic() {
        initDuration();
        handler.postDelayed(updateSeekBar, 1000);
    }

    void setupMediaPlayer(String url) throws IOException {
        mediaPlayer = MediaPlayer.create(this, R.raw.anthan);
        // mediaPlayer.setDataSource(url);
        mediaPlayer.setOnCompletionListener(mp -> {
            if (mp != null) {
                onCompleteMusic();
            }
        });
        /* mediaPlayer.prepareAsync();
        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
        audioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        audioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
        mediaPlayer.setAudioAttributes(audioAttributesBuilder.build()); */
    }

    void transSongByViewPager() {
        currentSong = songList.get(currentSongPosition);
        prepareMusic(currentSong.getUrl());
    }

    void sendBroadCastTransSong() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, currentSongPosition);
        intent.setAction(MediaPlayerReceiver.ACTION_TRANS_SONG);
        sendBroadcast(intent);
    }

    void onCompleteMusic() {
        if (currentRepeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ONCE)) {
            prepareMusic(currentSong.getUrl());
        } else if (currentRepeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ALL)) {
            transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        } else {
            if (currentSongPosition == songList.size() - 1) {
                handler.removeCallbacks(updateSeekBar);
                mediaPlayer.stop();
                Intent intent = new Intent((MediaPlayerReceiver.ACTION_MUSIC_COMPLETE));
                sendBroadcast(intent);
            } else {
                transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
            }
        }
    }

    void  transSong(int ACTION) {
        if (ACTION == Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG) {
            ++currentSongPosition;
            if (currentSongPosition == songList.size()) {
                currentSongPosition = 0;
            }
        } else {
            if (currentSongPosition == 0) {
                currentSongPosition = songList.size() - 1;
            } else {
                --currentSongPosition;
            }
        }
        sendBroadCastTransSong();
        currentSong = songList.get(currentSongPosition);
        prepareMusic(currentSong.getUrl());
    }

    void setMediaSessionCallBack() {
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
            }

            @Override
            public void onPause() {
                pauseMusic();
                super.onPause();
                // Handle pause action
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                // Handle skip to next action
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
            }

            @Override
            public void onStop() {
                super.onStop();
            }
        });
    }

    void seekMedia(int position) {
        mediaPlayer.seekTo(position);
    }

    void initDuration() {
        Intent intent = new Intent(MediaPlayerReceiver.ACTION_INIT_DURATION);
        String fullDuration = TimeFormatter.formatMillisecondToMinuteAndSecond(mediaPlayer.getDuration());
        intent.putExtra("fullIntDuration", mediaPlayer.getDuration());
        intent.putExtra("fullDuration", fullDuration);
        sendBroadcast(intent);
    }

    void updateDuration() {
        Intent intent = new Intent(MediaPlayerReceiver.ACTION_UPDATE_DURATION);
        String currentDuration = TimeFormatter.formatMillisecondToMinuteAndSecond(mediaPlayer.getCurrentPosition());
        int positionSeekbar = getPositionSeekbar(mediaPlayer.getCurrentPosition());
        intent.putExtra("positionSeekbar", positionSeekbar);
        intent.putExtra("currentDuration", currentDuration);
        sendBroadcast(intent);
    }

    int getPositionSeekbar(int position) {
        float currentPosition = (((float) position / mediaPlayer.getDuration()) * 1000);
        return (int) currentPosition;
    }

    void handleAction(int action) {
        if (action == Constants.MEDIA_PLAYER_ACTION_RESUME) {
            resumeMusic();
        } else if (action == Constants.MEDIA_PLAYER_ACTION_PAUSE) {
            pauseMusic();
        }
    }

    void resumeMusic() {
        changState(PlaybackStateCompat.STATE_PLAYING);
        handler.postDelayed(updateSeekBar, 0);
        mediaPlayer.start();
    }

    void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    void pauseMusic() {
        handler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                changState(PlaybackStateCompat.STATE_PAUSED);
                mediaPlayer.pause();
            }
        }
    }

    void changState(int state) {
        mediaSession.setPlaybackState(createPlaybackState(state));
        notificationBuilder.clearActions();
        notificationBuilder.addAction(R.drawable.ic_prev, "Previous", null);
        if (state == PlaybackStateCompat.STATE_PAUSED) {
            notificationBuilder.addAction(R.drawable.ic_play, "Pause", null)
                    .addAction(R.drawable.ic_next, "Next", null);
        } else {
            notificationBuilder.addAction(R.drawable.ic_pause, "Pause", null)
                    .addAction(R.drawable.ic_next, "Next", null);
        }
        Notification notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    PlaybackStateCompat createPlaybackState(int playBackState) {
        return new PlaybackStateCompat.Builder()
                .setState(playBackState, mediaPlayer.getCurrentPosition(), 1f, SystemClock.elapsedRealtime())
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void initNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noti);
        notificationBuilder = new NotificationCompat.Builder(this,
                String.valueOf(R.string.ID_CHANEL_MUSIC))
                .setSmallIcon(R.drawable.ic_noti)
                .setLargeIcon(bitmap)
                .setOnlyAlertOnce(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken()));
    }
}
