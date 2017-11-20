package com.funix.prm391x.se00255x.alarm.ui.view;

import com.funix.prm391x.se00255x.alarm.data.Alarm;

import java.util.ArrayList;

import static com.funix.prm391x.se00255x.timepickerwithsecond.TimePickerDialog.OnTimeSetListener;

public interface AlarmView {
    void showTimePicker(OnTimeSetListener onTimeSet);

    void setItems(ArrayList<Alarm> listOfAlarms);

    void addItem(Alarm item);

    void replaceItem(int position, Alarm item);

    void removeItem(Alarm item);
}
