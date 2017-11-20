package com.funix.prm391x.se00255x.alarm.ui.presenter;

import android.content.Context;

import com.funix.prm391x.se00255x.alarm.data.Alarm;
import com.funix.prm391x.se00255x.alarm.data.AlarmDB;
import com.funix.prm391x.se00255x.alarm.ui.view.AlarmView;
import com.funix.prm391x.se00255x.alarm.utils.AlarmMgr;

import java.util.ArrayList;
import java.util.Random;

public class MainPresenterImpl implements MainPresenter {
    private AlarmView mAlarmView;
    private AlarmDB mAlarmDB;
    private AlarmMgr mAlarmMgr;
    private ArrayList<Alarm> mListOfAlarms;

    private static int randomInt() {
        return new Random().nextInt(1_000_000);
    }

    public MainPresenterImpl(AlarmView alarmView) {
        mAlarmView = alarmView;
        Context context = (Context) alarmView;
        mAlarmDB = new AlarmDB(context);
        mAlarmMgr = new AlarmMgr(context);

        mListOfAlarms = mAlarmDB.query();
        for (Alarm alarm : mListOfAlarms) {
            mAlarmMgr.set(alarm);
        }
        mAlarmView.setItems(mListOfAlarms);
    }

    @Override
    public void createAlarm(long timeInMillis) {
        Alarm alarm;
        long result;
        do {
            alarm = new Alarm(randomInt(), timeInMillis);
            result = mAlarmDB.insert(alarm);
        } while (result < 0);
        mAlarmMgr.set(alarm);
        mAlarmView.addItem(alarm);
    }

    @Override
    public void updateAlarm(int position, long timeInMillis) {
        int requestCode = mListOfAlarms.get(position).getRequestCode();
        Alarm alarm = new Alarm(requestCode, timeInMillis);
        mAlarmMgr.set(alarm);
        mAlarmView.replaceItem(position, alarm);
    }

    @Override
    public void removeAlarm(int position) {
        Alarm alarm = mListOfAlarms.get(position);
        mAlarmMgr.cancel(alarm);
        mAlarmDB.delete(alarm);
        mAlarmView.removeItem(alarm);
    }
}
