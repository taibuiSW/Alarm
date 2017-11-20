package com.funix.prm391x.se00255x.alarm.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.funix.prm391x.se00255x.alarm.data.Alarm;
import com.funix.prm391x.se00255x.alarm.R;

import java.util.ArrayList;

import static android.view.View.OnClickListener;

public class CustomAdapter extends ArrayAdapter<Alarm> {
    private ArrayList<Alarm> mListOfAlarms;
    private LayoutInflater mInflater;
    private AlarmItemInteract mInteract;

    public CustomAdapter(Context context, ArrayList<Alarm> listOfAlarms) {
        super(context, R.layout.single_alarm, listOfAlarms);
        mListOfAlarms = listOfAlarms;
        mInflater = LayoutInflater.from(context);
        mInteract = (AlarmItemInteract) context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_alarm, parent, false);
            holder = new ViewHolder();
            holder.mTimeView = convertView.findViewById(R.id.time_view);
            holder.mAmPmView = convertView.findViewById(R.id.amPm_view);
            holder.mDeleteBtn = convertView.findViewById(R.id.delete_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String time = mListOfAlarms.get(position).getFormattedTime(); // "hh:mm:ss a"
        holder.mTimeView.setText(time.substring(0, 8)); // "hh:mm:ss"
        holder.mAmPmView.setText(time.substring(8, 11)); // " am" or " pm"

        // update existing alarm
        holder.mTimeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInteract.onItemClick(position);
            }
        });

        // delete alarm when press delete button
        holder.mDeleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInteract.onDeleteButtonClick(position);
            }
        });
        return convertView;
    }

    public void replace(int position, Alarm item) {
        mListOfAlarms.set(position, item);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView mTimeView;
        TextView mAmPmView;
        Button mDeleteBtn;
    }
}
