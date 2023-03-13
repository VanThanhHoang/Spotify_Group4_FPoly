package com.example.spotify_group4.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
    PlaybackStateCompat.Builder playbackStateBuilder;
    int currentPosition = 0;
    void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "MEDIA");
        mediaSession.setActive(true);
        // set info
        metadataBuilder = new MediaMetadataCompat.Builder();
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist");
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Song Title");
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album Name");
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(getResources(), R.drawable.ic_facebook));
        mediaSession.setMetadata(metadataBuilder.build());

        setMediaSessionCallBack();

        playbackStateBuilder = new PlaybackStateCompat.Builder();
        playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0, 0);
        playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, currentPosition, 1.0f, SystemClock.elapsedRealtime());
        mediaSession.setPlaybackState(playbackStateBuilder.build());
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
      /*  try {*/
            mediaPlayer = new MediaPlayer().create(this,R.raw.homlayemcuoi);
           /* mediaPlayer.setDataSource(url);*/
            /*mediaPlayer.prepare();*/
            mediaPlayer.start();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    initDuration();
                    handler.postDelayed(updateSeekBar, 1000);
                    initMediaSession();
                    sendNotificationMediaPlayer();
                }
            });

        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    void setMediaSessionCallBack() {
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                playMusic();
                mediaSession.setPlaybackState(playbackStateBuilder.build());
            }

            @Override
            public void onPause() {
                pauseMusic();
                super.onPause();
                // Handle pause action
                mediaSession.setPlaybackState(playbackStateBuilder.build());
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                // Handle skip to next action
                mediaSession.setPlaybackState(playbackStateBuilder.build());
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                // Handle skip to previous action
                mediaSession.setPlaybackState(playbackStateBuilder.build());
            }

            @Override
            public void onStop() {
                stopMusic();
                super.onStop();
            }
        });
    }

    void seekMedia(int position) {
        mediaPlayer.seekTo(position);
    }

    void initDuration() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_INIT_DURATION);
        String fullDuration = formatTime(mediaPlayer.getDuration());
        intent.putExtra("fullIntDuration", mediaPlayer.getDuration());
        intent.putExtra("fullDuration", fullDuration);
        sendBroadcast(intent);
    }

    void updateDuration() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_UPDATE_DURATION);
        currentPosition = mediaPlayer.getCurrentPosition();
        String currentDuration = formatTime(currentPosition);
        int positionSeekbar = getPositionSeekbar(currentPosition);
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
        String url = intent.getStringExtra("URL");
        int positionToSeek = intent.getIntExtra("POSITION_TO_SEEK", 0);
        if (!isExits) {
            isExits = true;
            prepareMusic(url);
        }
        if (action == MediaPlayerPresenter.ACTION_SEEK) {
            seekMedia(positionToSeek);
        }
        //action
        handleAction(action);
        return START_NOT_STICKY;
    }

    void handleAction(int action) {
        if (action == MediaPlayerPresenter.ACTION_PLAY || action == MediaPlayerPresenter.ACTION_RESUME) {
            playMusic();
        } else if (action == MediaPlayerPresenter.ACTION_STOP) {
            stopMusic();
        } else if (action == MediaPlayerPresenter.ACTION_PAUSE) {
            pauseMusic();
        }
    }

    void playMusic() {
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
                mediaPlayer.pause();
            }
        }
    }

    void sendNotificationMediaPlayer() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noti);
        Notification notification = new NotificationCompat.Builder(this,
                String.valueOf(R.string.ID_CHANEL_MUSIC))
                .setSmallIcon(R.drawable.ic_noti)
                .setSubText("Sky music")
                .setLargeIcon(bitmap)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_prev, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken()))
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int duration) {
        int minutes = (duration / 1000) / 60; // tính số phút
        int seconds = (duration / 1000) % 60; // tính số giây
        return String.format("%02d:%02d", minutes, seconds);
    }
}
