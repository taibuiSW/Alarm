package com.funix.prm391x.se00255x.alarm.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.funix.prm391x.se00255x.alarm.data.Alarm;
import com.funix.prm391x.se00255x.alarm.data.AlarmDB;
import com.funix.prm391x.se00255x.alarm.service.AlarmReceiver;

public class AlarmMgr {
    private Context mContext;
    private AlarmManager mAlarmManager;

    public AlarmMgr(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void set(Alarm alarm) {
        int requestCode = alarm.getRequestCode();
        long timeInMillis = alarm.getTimeInMillis();
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(AlarmDB.REQUEST_CODE, requestCode);
        intent.putExtra(AlarmDB.TIME_IN_MILLIS, timeInMillis);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public void cancel(Alarm alarm) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, alarm.getRequestCode(), new Intent(mContext, AlarmReceiver.class), 0);
        mAlarmManager.cancel(pendingIntent);
    }
}
