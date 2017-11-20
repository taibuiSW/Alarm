package com.funix.prm391x.se00255x.alarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.funix.prm391x.se00255x.alarm.data.Alarm;
import com.funix.prm391x.se00255x.alarm.data.AlarmDB;
import com.funix.prm391x.se00255x.alarm.utils.AlarmMgr;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            AlarmMgr alarmMgr = new AlarmMgr(context);
            ArrayList<Alarm> alarms = new AlarmDB(context).query();
            for (Alarm a : alarms) {
                alarmMgr.set(a);
            }
        } else {
            Intent serviceIntent = new Intent(context, AlarmService.class);
            serviceIntent.putExtra(AlarmDB.TABLE_ALARM, intent.getExtras());
            context.startService(serviceIntent);
        }
    }
}
