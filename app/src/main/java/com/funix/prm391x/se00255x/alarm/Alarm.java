package com.funix.prm391x.se00255x.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Alarm {
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("hh:mm:ss a", Locale.US);

    private Context mContext;

    int getRequestCode() {
        return mRequestCode;
    }

    long getTimeInMillis() {
        return mTimeInMillis;
    }

    private int mRequestCode;
    private long mTimeInMillis;
    private String mFormattedTime;

    Alarm(Context context, int requestCode, long timeInMillis) {
        this.mContext = context;
        this.mRequestCode = requestCode;

        // prevent alarm from firing immediately
        while (timeInMillis < Calendar.getInstance().getTimeInMillis()) {
            timeInMillis += AlarmManager.INTERVAL_DAY;
        }
        this.mTimeInMillis = timeInMillis;
        this.mFormattedTime = TIME_FORMAT.format(new Date(mTimeInMillis));
    }

    void set() {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(AlarmDB.REQUEST_CODE, mRequestCode);
        intent.putExtra(AlarmDB.TIME_IN_MILLIS, mTimeInMillis);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, mRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        getAlarmManager().setExact(AlarmManager.RTC_WAKEUP, mTimeInMillis, pendingIntent);
    }

    void cancel() {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, mRequestCode, new Intent(mContext, AlarmReceiver.class), 0);
        getAlarmManager().cancel(pendingIntent);
    }

    String getFormattedTime() {
        return mFormattedTime;
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }
}

