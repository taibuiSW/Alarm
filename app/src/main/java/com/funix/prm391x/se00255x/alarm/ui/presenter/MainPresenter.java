package com.funix.prm391x.se00255x.alarm.ui.presenter;

public interface MainPresenter {
    void createAlarm(long timeInMillis);

    void updateAlarm(int position, long timeInMillis);

    void removeAlarm(int position);
}
