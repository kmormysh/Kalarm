package com.samples.katy.kalarm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Katy on 3/17/2015.
 */
public class AlarmAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private static List<Alarm> alarmList;

    public AlarmAdapter(Context context, List<Alarm> alarmList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.alarmList = alarmList;
    }


    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Alarm getAlarm(int position) {
        return (Alarm) getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Alarm alarm = getAlarm(position);

        final int pos = position;

        if (view == null)
            view = layoutInflater.inflate(R.layout.alarm_row, parent, false);

        final Switch alarmSwitch = (Switch) view.findViewById(R.id.on_off_switch);

        alarmSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarmList();
                Alarm newAlarm = getAlarm(pos);

                if (alarmSwitch.isChecked()) { //On
                    newAlarm.setEnable(true);
                    MainActivity.alarmDatabaseHandler.updateAlarm(newAlarm);
                    MainActivity.alarmManagerReceiver.setAlarms(MainActivity.context);
                } else { //Off
                    newAlarm.setEnable(false);
                    MainActivity.alarmDatabaseHandler.updateAlarm(newAlarm);
                    MainActivity.alarmManagerReceiver.cancelAlarm(MainActivity.context);
                }

                notifyDataSetChanged();
            }
        });

//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                getAlarmList();
//                Alarm newAlarm = getAlarm(pos);
//                MainActivity.alarmManagerReceiver.cancelAlarm(MainActivity.context);
//                MainActivity.alarmDatabaseHandler.deleteAlarm(newAlarm);
//                getAlarmList();
//
//                notifyDataSetChanged();
//                return false;
//            }
//        });


        TextView alarm_time = (TextView) view.findViewById(R.id.alarm_time);
        TextView alarm_name = (TextView) view.findViewById(R.id.alarm_name);
        TextView alarm_days = (TextView) view.findViewById(R.id.alarm_days);

        alarmSwitch.setChecked(alarm.getEnable());
        alarm_time.setText(alarm.getAlarm_time());
        alarm_time.setTextColor(Color.WHITE);
        alarm_name.setText(alarm.getAlarm_name());
        alarm_name.setTextColor(Color.WHITE);
        alarm_days.setText(alarm.getAlarm_day());
        alarm_days.setTextColor(Color.WHITE);

        return view;
    }

    public static void getAlarmList () {
        alarmList = MainActivity.alarmDatabaseHandler.getAllAlarms();
    }
}
