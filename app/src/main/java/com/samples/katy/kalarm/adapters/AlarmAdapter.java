package com.samples.katy.kalarm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.samples.katy.kalarm.models.Alarm;
import com.samples.katy.kalarm.utils.AlarmDatabaseHandler;
import com.samples.katy.kalarm.utils.AlarmManagerReceiver;
import com.samples.katy.kalarm.R;

import java.util.List;

public class AlarmAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Alarm> alarmList;
    private AlarmDatabaseHandler alarmDatabaseHandler;
    private AlarmManagerReceiver alarmManagerReceiver;

    public AlarmAdapter(Context context, List<Alarm> alarmList, AlarmDatabaseHandler alarmDatabaseHandler,
                        AlarmManagerReceiver alarmManagerReceiver) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.alarmList = alarmList;
        this.alarmDatabaseHandler = alarmDatabaseHandler;
        this.alarmManagerReceiver = alarmManagerReceiver;
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
        final Context context = parent.getContext();

        if (view == null)
            view = layoutInflater.inflate(R.layout.alarm_row, parent, false);

        final Switch alarmSwitch = (Switch) view.findViewById(R.id.on_off_switch);

        alarmSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlarmList();
                Alarm newAlarm = getAlarm(pos);

                newAlarm.setEnabled(alarmSwitch.isChecked());
                alarmDatabaseHandler.updateAlarm(newAlarm);
                alarmManagerReceiver.setAlarms(context);

                notifyDataSetChanged();
            }
        });

        TextView alarm_time = (TextView) view.findViewById(R.id.alarm_time);
        TextView alarm_name = (TextView) view.findViewById(R.id.alarm_name);
        TextView alarm_days = (TextView) view.findViewById(R.id.alarm_days);

        alarmSwitch.setChecked(alarm.getEnabled());
        alarm_time.setText(String.format("%02d:%02d", alarm.getAlarmHours(), alarm.getAlarmMinutes()));
        alarm_name.setText(alarm.getAlarmName());

        alarm_days.setText(convertDaysIntToString(alarm.getDays(), context));

        return view;
    }

    public void getAlarmList() {
        alarmList = alarmDatabaseHandler.getAllAlarms();
    }

    private String convertDaysIntToString(boolean[] days, Context context) {
        int[] dayNames = {R.string.monday, R.string.tuesday, R.string.wednesday,
                R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday};
        String result = "";
        for (int i = 0; i < days.length; i++) {
            if (days[i]) {
                result += context.getString(dayNames[i]) + " ";
            }
        }
        return result;
    }
}
