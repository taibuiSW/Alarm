package com.funix.prm391x.se00255x.alarm.ui.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.funix.prm391x.se00255x.alarm.R;
import com.funix.prm391x.se00255x.alarm.data.Alarm;
import com.funix.prm391x.se00255x.alarm.ui.presenter.MainPresenterImpl;
import com.funix.prm391x.se00255x.alarm.utils.TimeUtils;
import com.funix.prm391x.se00255x.timepickerwithsecond.TimePicker;
import com.funix.prm391x.se00255x.timepickerwithsecond.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static com.funix.prm391x.se00255x.timepickerwithsecond.TimePickerDialog.OnTimeSetListener;

public class MainActivity extends AppCompatActivity implements AlarmView, AlarmItemInteract {
    private MainPresenterImpl mPresenter;
    private ListView mListView;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.lsv_alarms);
        mPresenter = new MainPresenterImpl(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_alarm_btn:
                newAlarm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void newAlarm() {
        showTimePicker(new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker tp, int h, int m, int s) {
                mPresenter.createAlarm(TimeUtils.convertTimeToMillis(h, m, s));
            }
        });
    }

    @Override
    public void showTimePicker(OnTimeSetListener onTimeSet) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, onTimeSet,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                false).show();
    }

    @Override
    public void setItems(ArrayList<Alarm> listOfAlarms) {
        mAdapter = new CustomAdapter(this, listOfAlarms);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void addItem(Alarm item) {
        mAdapter.add(item);
    }

    @Override
    public void replaceItem(int position, Alarm item) {
        mAdapter.replace(position, item);
    }

    @Override
    public void removeItem(Alarm item) {
        mAdapter.remove(item);
    }

    @Override
    public void onItemClick(int position) {
        updateAlarm(position);
    }

    private void updateAlarm(final int position) {
        showTimePicker(new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker tp, int h, int m, int s) {
                mPresenter.updateAlarm(position, TimeUtils.convertTimeToMillis(h, m, s));
            }
        });
    }

    @Override
    public void onDeleteButtonClick(int position) {
        mPresenter.removeAlarm(position);
    }
}