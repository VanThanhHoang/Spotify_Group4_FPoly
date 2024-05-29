package com.example.spotify_group4.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        String action = intent.getStringExtra(Constants.ACTION_MEDIA_PLAYER);
        Log.d("123", "action" + action);
        if (action == null) {
            action = intent.getAction();
        }
        if (action.equals(Constants.MEDIA_PLAYER_ACTION_CONTINUES_MEDIA_PLAYER)) {
            continuesMediaPlayer();
            return START_NOT_STICKY;
        }
        if (action.equals(Constants.MEDIA_PLAYER_ACTION_PLAY_LIST_SONG) && !isInitPlayPlayList) {
            initPlayList(intent);
            return START_NOT_STICKY;
        }
        if (action.equals(Constants.MEDIA_PLAYER_ACTION_CHANGE_REPEAT_MODE)) {
            changeRepeatMode();
            return START_NOT_STICKY;
        } else if (action.equals(Constants.MEDIA_PLAYER_ACTION_TRANS_SONG_VIEWPAGER)) {
            currentSongPosition = intent.getIntExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, 0);
            transSongByViewPager();
        } else {
            handlerMediaPlayer(action, intent);
        }
        return START_NOT_STICKY;
    }

    void changeRepeatMode() {
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
    }

    void handlerMediaPlayer(String action, Intent intent) {
        if (action.equals(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG)) {
            Log.d("123", "handlerMediaPlayer: ");
            transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        } else if (action.equals(Constants.MEDIA_PLAYER_ACTION_PLAY_PREV_SONG)) {
            transSong(Constants.MEDIA_PLAYER_ACTION_PLAY_PREV_SONG);
        } else if (action.equals(Constants.MEDIA_PLAYER_ACTION_SEEK)) {
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
        setupMediaPlayer(currentSong.getUrl());
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
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
        Picasso.get()
                .load(currentSong.getUrlImg())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
                        mediaSession.setMetadata(metadataBuilder.build());
                        setMediaSessionCallBack();
                        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // called when the bitmap failed to load
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // called when the image is being fetched or loaded
                    }
                });
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
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


    void transSongByViewPager() {
        currentSong = songList.get(currentSongPosition);
        setupMediaPlayer(currentSong.getUrl());
    }

    void sendBroadCastTransSong() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, currentSongPosition);
        intent.setAction(MediaPlayerReceiver.ACTION_TRANS_SONG);
        intent.setAction(MediaPlayerReceiver.ACTION_TRANS_SONG);
        sendBroadcast(intent);
    }

    void onCompleteMusic() {
        if (currentRepeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ONCE)) {
            setupMediaPlayer(currentSong.getUrl());
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

    void transSong(String ACTION) {
        if (ACTION.equals(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG)) {
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
        setupMediaPlayer(currentSong.getUrl());
    }

    void setMediaSessionCallBack() {
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                resumeMusic();
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

    void continuesMediaPlayer() {
        sendListShuffled();
        initDuration();
        resumeMusic();
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

    void handleAction(String action) {
        if (action.equals(Constants.MEDIA_PLAYER_ACTION_RESUME)) {
            sendBroadCastResumesMusic();
            resumeMusic();
        } else if (action.equals(Constants.MEDIA_PLAYER_ACTION_PAUSE)) {
            sendBroadCastPauseMusic();
            pauseMusic();
        }
    }

    void resumeMusic() {
        if (!mediaPlayer.isPlaying()) {
            changeState(PlaybackStateCompat.STATE_PLAYING);
            handler.postDelayed(updateSeekBar, 0);
            mediaPlayer.start();
        }
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
                changeState(PlaybackStateCompat.STATE_PAUSED);
                mediaPlayer.pause();
            }
        }
    }

    void sendBroadCastPauseMusic() {
        Intent intent = new Intent(MediaPlayerReceiver.ACTION_PAUSE_MUSIC);
        sendBroadcast(intent);
    }

    void sendBroadCastResumesMusic() {
        Intent intent = new Intent(MediaPlayerReceiver.ACTION_RESUME_MUSIC);
        sendBroadcast(intent);
    }

    void changeState(int state) {
        mediaSession.setPlaybackState(createPlaybackState(state));
        notificationBuilder.clearActions();
        Intent prevIntent = new Intent(this, MediaPlayerService.class);
        prevIntent.setAction(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.addAction(R.drawable.ic_prev, "Previous", pendingPrevIntent);

        if (state == PlaybackStateCompat.STATE_PAUSED) {
            Intent resumeIntent = new Intent(this, MediaPlayerService.class);
            resumeIntent.setAction(Constants.MEDIA_PLAYER_ACTION_RESUME);
            PendingIntent pendingResumeIntent = PendingIntent.getService(this, 0, resumeIntent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.addAction(R.drawable.ic_play, "Pause", pendingResumeIntent);
        } else {
            Intent pauseIntent = new Intent(this, MediaPlayerService.class);
            pauseIntent.setAction(Constants.MEDIA_PLAYER_ACTION_PAUSE);
            PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.addAction(R.drawable.ic_pause, "Pause", pendingPauseIntent);

        }
        Intent nextIntent = new Intent(this, MediaPlayerService.class);
        nextIntent.setAction(Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        PendingIntent pendingNextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.addAction(R.drawable.ic_next, "Next", pendingNextIntent);


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
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()));
    }

    void setupMediaPlayer(String url) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
//        mediaPlayer = MediaPlayer.create(this, R.raw.roimotngay);
//         mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(url));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            return;
        }
        mediaPlayer.setOnErrorListener((mp, what, extra) -> true);

        mediaPlayer.setOnCompletionListener(mp -> {
            if (mp != null) {
                onCompleteMusic();
            }
        });
        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            startUiMusic();
            initMediaSession();
            initNotification();
            changeState(PlaybackStateCompat.STATE_PLAYING);
        });
    }

}
