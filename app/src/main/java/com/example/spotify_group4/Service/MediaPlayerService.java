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
import java.util.List;


public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;
    Handler handler;
    boolean isInitSong;
    MediaSessionCompat mediaSession;
    private Runnable updateSeekBar;
    MediaMetadataCompat.Builder metadataBuilder;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    int currentSongPosition;
    List<Song> songList;
    Song currentSong;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("ACTION", 0);
        if (!isInitSong) {
            currentSongPosition = intent.getIntExtra("CURRENT_SONG_POSITION", 0);
        }
        songList = intent.getParcelableArrayListExtra("SONG_LIST");
        currentSong = songList.get(currentSongPosition);
        if(action==MediaPlayerPresenter.ACTION_TRANS_SONG_VIEWPAGER){
            currentSongPosition = intent.getIntExtra("CURRENT_SONG_POSITION",0);
            transSongByViewPager();
        }
        if (songList.isEmpty()) {
            return START_NOT_STICKY;
        }
        if (!isInitSong) {
            isInitSong = true;
            prepareMusic(currentSong.getUrl());
        }
        if (action == MediaPlayerPresenter.ACTION_PLAY_NEXT_SONG) {
            transSong(MediaPlayerPresenter.ACTION_PLAY_NEXT_SONG);
        } else if (action == MediaPlayerPresenter.ACTION_PLAY_PREV_SONG) {
            transSong(MediaPlayerPresenter.ACTION_PLAY_PREV_SONG);
        } else if (action == MediaPlayerPresenter.ACTION_SEEK) {
            int positionToSeek = intent.getIntExtra("POSITION_TO_SEEK", 0);
            mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PLAYING));
            seekMedia(positionToSeek);
        } else {
            handleAction(action);
        }
        //action
        return START_NOT_STICKY;
    }

    void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "MEDIA");
        mediaSession.setActive(true);
        // set info
        metadataBuilder = new MediaMetadataCompat.Builder();
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
    public void onCreate() {
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
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    void prepareMusic(String url) {
        initMediaSession();
        initNotification();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        try {
            setupMediaPlayer(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                startUiMusic();
                mediaPlayer.start();
                changState(PlaybackStateCompat.STATE_PLAYING);
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
    }

    void setupMediaPlayer(String url) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setOnCompletionListener(mp -> {
            if (mp != null) {
                changState(PlaybackStateCompat.STATE_PAUSED);
                onCompleteMusic();
                handler.removeCallbacks(updateSeekBar);
            }
        });
        mediaPlayer.prepareAsync();
        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
        audioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        audioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
        mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
    }
    void transSongByViewPager() {
        currentSong = songList.get(currentSongPosition);
        prepareMusic(currentSong.getUrl());
    }
    void transSong(int ACTION) {
        if (ACTION == MediaPlayerPresenter.ACTION_PLAY_NEXT_SONG) {
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

    void sendBroadCastTransSong() {
        Intent intent = new Intent();
        intent.putExtra("CURS_POSITION", currentSongPosition);
        intent.setAction(MusicPlayFragment.ACTION_TRANS_SONG);
        sendBroadcast(intent);
    }

    void onCompleteMusic() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_MUSIC_COMPLETE);
        sendBroadcast(intent);
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

    void changState(int state) {
        mediaSession.setPlaybackState(createPlaybackState(PlaybackStateCompat.STATE_PAUSED));
        notificationBuilder.clearActions();
        notificationBuilder.addAction(R.drawable.ic_prev, "Previous", null);
        if (state == PlaybackStateCompat.STATE_PAUSED) {
            notificationBuilder.addAction(R.drawable.ic_play, "Pause", null)
                    .addAction(R.drawable.ic_next, "Next", null);
        } else {
            notificationBuilder.addAction(R.drawable.ic_pause, "Pause", null)
                    .addAction(R.drawable.ic_next, "Next", null);
        }
        notification = notificationBuilder.build();
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
}
