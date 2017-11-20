package com.funix.prm391x.se00255x.alarm.data;

import android.app.AlarmManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Alarm {
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("hh:mm:ss a", Locale.US);

    private int mRequestCode;
    private long mTimeInMillis;

    public int getRequestCode() {
        return mRequestCode;
    }

    public long getTimeInMillis() {
        return mTimeInMillis;
    }

    public Alarm(int requestCode, long timeInMillis) {
        mRequestCode = requestCode;

        // prevent alarm from firing immediately
        while (timeInMillis < Calendar.getInstance().getTimeInMillis()) {
            timeInMillis += AlarmManager.INTERVAL_DAY;
        }
        mTimeInMillis = timeInMillis;
    }

    public String getFormattedTime() {
        return TIME_FORMAT.format(new Date(mTimeInMillis));
    }
}

