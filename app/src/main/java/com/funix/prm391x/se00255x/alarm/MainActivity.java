package com.funix.prm391x.se00255x.alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.funix.prm391x.se00255x.timepickerwithsecond.TimePicker;
import com.funix.prm391x.se00255x.timepickerwithsecond.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Context mCtx = this;
    private ArrayList<Alarm> mListOfAlarms = new ArrayList<>();
    private CustomAdapter mAdapter;
    private AlarmDB mAlarmDB;

    private static int getRandomId() {
        return new Random().nextInt(1_000_000_000);
    }

    private static long getTimeInMillis(int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlarmDB = new AlarmDB(mCtx);
        mListOfAlarms = mAlarmDB.load();
        mAdapter = new CustomAdapter(mCtx, R.layout.single_alarm, R.id.time_view, mListOfAlarms);
        ListView mListView = (ListView) findViewById(R.id.alarms_listview);
        mListView.setAdapter(mAdapter);
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
                addAlarmAtPosition(mListOfAlarms.size());
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAlarmAtPosition(final int position) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(mCtx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int second) {
                createAlarm(position, hourOfDay, minute, second);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), false).show();
    }

    private void createAlarm(int position, int hourOfDay, int minute, int second) {
        long timeInMillis = getTimeInMillis(hourOfDay, minute, second);
        if (position < mListOfAlarms.size()) {
            updateAlarm(position, timeInMillis);
        } else {
            createAlarm(timeInMillis);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateAlarm(int position, long timeInMillis) {
        int requestCode = mListOfAlarms.get(position).getRequestCode();
        Alarm alarm = new Alarm(mCtx, requestCode, timeInMillis);
        alarm.activate();
        mListOfAlarms.set(position, alarm);
        mAlarmDB.update(alarm);
    }

    private void createAlarm(long timeInMillis) {
        Alarm alarm;
        long result;
        do {
            alarm = new Alarm(mCtx, getRandomId(), timeInMillis);
            result = mAlarmDB.insert(alarm);
        } while (result < 0);
        alarm.activate();
        mListOfAlarms.add(alarm);
    }

    private static class ViewHolder {
        TextView timeView;
        TextView amPmView;
        Button deleteBtn;
    }

    private class CustomAdapter extends ArrayAdapter<Alarm> {
        LayoutInflater mInflater;

        CustomAdapter(@NonNull Context context, @LayoutRes int resource,
                      @IdRes int textViewResourceId, @NonNull List<Alarm> objects) {
            super(context, resource, textViewResourceId, objects);
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_alarm, parent, false);
                holder = new ViewHolder();
                holder.timeView = (TextView) convertView.findViewById(R.id.time_view);
                holder.amPmView = (TextView) convertView.findViewById(R.id.amPm_view);
                holder.deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String time = mListOfAlarms.get(position).getFormattedTime(); // "hh:mm:ss a"
            holder.timeView.setText(time.substring(0, 8)); // "hh:mm:ss"
            holder.amPmView.setText(time.substring(8, 11)); // " am" or " pm"

            // update existing alarm
            holder.timeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAlarmAtPosition(position);
                }
            });

            // delete alarm when press delete button
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alarm alarm = mListOfAlarms.get(position);
                    alarm.cancel();
                    mAlarmDB.delete(alarm);
                    mListOfAlarms.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}