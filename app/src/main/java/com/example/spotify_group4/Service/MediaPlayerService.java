package com.example.spotify_group4.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.spotify_group4.Helper.TimeFormatter;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Fragment.MusicPlayFragment;

import java.io.IOException;


public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;
    boolean isExits = false;
    Handler handler;
    MediaSessionCompat mediaSession;
    private Runnable updateSeekBar;
    MediaMetadataCompat.Builder metadataBuilder;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    Song song  ;
    void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "MEDIA");
        mediaSession.setActive(true);
        // set info
        metadataBuilder = new MediaMetadataCompat.Builder();
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getSingerName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getName());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album Name");
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(getResources(), R.drawable.ic_facebook));
        mediaSession.setMetadata(metadataBuilder.build());
        setMediaSessionCallBack();
        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
    }

    @Override
    public void onCreate() {
        handler = new Handler();
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
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    void prepareMusic(String url) {
        try {
            setupMediaPlayer(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                startUiMusic();
                initNotification();
                onPlayState();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startUiMusic() {
        handler.postDelayed(updateSeekBar, 1000);
        mediaPlayer.start();
        initDuration();
        handler.postDelayed(updateSeekBar, 1000);
        initMediaSession();

    }

    void setupMediaPlayer(String url) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
        audioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        audioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
        mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
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
        Intent intent = new Intent(MusicPlayFragment.ACTION_INIT_DURATION);
        String fullDuration = TimeFormatter.formatMillisecondToMinuteAndSecond(mediaPlayer.getDuration());
        intent.putExtra("fullIntDuration", mediaPlayer.getDuration());
        intent.putExtra("fullDuration", fullDuration);
        sendBroadcast(intent);
    }

    void updateDuration() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_UPDATE_DURATION);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("ACTION", 0);
        song  = (Song) intent.getSerializableExtra("SONG");
        int positionToSeek = intent.getIntExtra("POSITION_TO_SEEK", 0);
        if (!isExits) {
            isExits = true;
            prepareMusic(song.getUrl());
        }
        if (action == MediaPlayerPresenter.ACTION_SEEK) {
            mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
            seekMedia(positionToSeek);
        } else {
            handleAction(action);
        }
        //action
        return START_NOT_STICKY;
    }

    void handleAction(int action) {
        if (action == MediaPlayerPresenter.ACTION_RESUME) {
            resumeMusic();
        } else if (action == MediaPlayerPresenter.ACTION_STOP) {
            stopMusic();
        } else if (action == MediaPlayerPresenter.ACTION_PAUSE) {
            pauseMusic();
        }
    }

    void resumeMusic() {
        onPlayState();
        handler.postDelayed(updateSeekBar, 1000);
        mediaPlayer.start();

    }

    void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
        }
    }

    void pauseMusic() {
        handler.removeCallbacks(updateSeekBar);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                onPauseState();
                mediaPlayer.pause();
            }
        }
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

    void onPlayState() {
        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
        notificationBuilder.clearActions();
        notificationBuilder.addAction(R.drawable.ic_prev, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null);
        notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    PlaybackStateCompat createPlaybackState(int playBackState) {
        return new PlaybackStateCompat.Builder()
                .setState(playBackState, mediaPlayer.getCurrentPosition(), 1f, SystemClock.elapsedRealtime())
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build();
    }

    void onPauseState() {
        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PAUSED));
        notificationBuilder.clearActions();
        notificationBuilder.addAction(R.drawable.ic_prev, "Previous", null)
                .addAction(R.drawable.ic_play, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null);
        notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
