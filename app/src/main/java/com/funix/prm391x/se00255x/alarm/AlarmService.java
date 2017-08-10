package com.funix.prm391x.se00255x.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

public class AlarmService extends Service {
    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mVibrator == null) {
            mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }
        if (intent.hasExtra(AlarmDB.TABLE_ALARM)) {
            playAlarmSound();
            displayNotification();
            mVibrator.vibrate(new long[] {1000, 1000, 1000}, 0);
            setRepeatAlarm(intent);
        } else {
            stopAlarmSound();
            mVibrator.cancel();
        }
        return START_NOT_STICKY;
    }

    private void playAlarmSound() {
        if (mMediaPlayer == null) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setDataSource(this,
                        Uri.parse("android.resource://" + getPackageName() + "/raw/dont_talk_anymore"));
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopAlarmSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        stopSelf();
    }

    private void displayNotification() {
        NotificationManager notificationMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationClick = new Intent(this, AlarmService.class);
        PendingIntent notificationClickPI = PendingIntent.getService(
                this, 0, notificationClick, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle("You have an alarm")
                .setContentText("Press to dismiss")
                .setContentIntent(notificationClickPI)
                .setSmallIcon(R.mipmap.ic_notification)
                .setOngoing(true)
                .setAutoCancel(true);
        notificationMgr.notify(0, notification.build());
    }

    // set a same alarm for the next day
    private void setRepeatAlarm(Intent intent) {
        Bundle bundle = intent.getBundleExtra(AlarmDB.TABLE_ALARM);
        int requestCode = bundle.getInt(AlarmDB.REQUEST_CODE);
        long timeInMillis = bundle.getLong(AlarmDB.TIME_IN_MILLIS);
        new Alarm(this, requestCode, timeInMillis, true);
    }
}

