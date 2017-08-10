package com.funix.prm391x.se00255x.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            new AlarmDB(context).load();
        } else {
            Intent serviceIntent = new Intent(context, AlarmService.class);
            serviceIntent.putExtra(AlarmDB.TABLE_ALARM, intent.getExtras());
            context.startService(serviceIntent);
        }
    }
}
