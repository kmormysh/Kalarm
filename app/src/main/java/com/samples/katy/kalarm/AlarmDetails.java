package com.samples.katy.kalarm;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmDetails extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Alarm alarm;
    ToggleButton btn_mon;
    ToggleButton btn_tue;
    ToggleButton btn_wed;
    ToggleButton btn_thu;
    ToggleButton btn_fri;
    ToggleButton btn_sat;
    ToggleButton btn_sun;
    EditText alarm_name;
    Button btn_save;

    private boolean[] repeatDays = new boolean[7];

    static AlarmDetails newInstance(int id){
        AlarmDetails alarmDetails = new AlarmDetails();
        Bundle arg = new Bundle();
        arg.putInt(Alarm.ID, id);
        alarmDetails.setArguments(arg);

        return alarmDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getAlarm();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.alarm_details_pop_up, container);

        btn_mon = (ToggleButton) view.findViewById(R.id.btn_mon);
        btn_mon.setChecked(alarm.getDays()[Calendar.MONDAY-1]);
        btn_mon.setOnClickListener(this);

        btn_tue = (ToggleButton) view.findViewById(R.id.btn_tue);
        btn_tue.setChecked(alarm.getDays()[Calendar.TUESDAY-1]);
        btn_tue.setOnClickListener(this);

        btn_wed = (ToggleButton) view.findViewById(R.id.btn_wed);
        btn_wed.setChecked(alarm.getDays()[Calendar.WEDNESDAY-1]);
        btn_wed.setOnClickListener(this);

        btn_thu = (ToggleButton) view.findViewById(R.id.btn_thu);
        btn_thu.setChecked(alarm.getDays()[Calendar.THURSDAY-1]);
        btn_thu.setOnClickListener(this);

        btn_fri = (ToggleButton) view.findViewById(R.id.btn_fri);
        btn_fri.setChecked(alarm.getDays()[Calendar.FRIDAY-1]);
        btn_fri.setOnClickListener(this);

        btn_sat = (ToggleButton) view.findViewById(R.id.btn_sat);
        btn_sat.setChecked(alarm.getDays()[Calendar.SATURDAY-1]);
        btn_sat.setOnClickListener(this);

        btn_sun = (ToggleButton) view.findViewById(R.id.btn_sun);
        btn_sun.setChecked(alarm.getDays()[Calendar.SUNDAY-1]);
        btn_sun.setOnClickListener(this);

        alarm_name = (EditText) view.findViewById(R.id.alarm_name);
        alarm_name.setText(alarm.getAlarm_name());
        alarm_name.setOnClickListener(this);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.alarm_time);
        timePicker.setCurrentHour(Integer.parseInt(alarm.getAlarm_time().substring(0, 2)));
        timePicker.setCurrentMinute(Integer.parseInt(alarm.getAlarm_time().substring(3, 5)));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarm.setAlarm_time(padding_str(hourOfDay) + ":" + padding_str(minute));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        dismiss();
    }

    public Alarm getAlarm() {
        alarm = MainActivity.alarmDatabaseHandler.getAlarm(getArguments().getInt(Alarm.ID));
        repeatDays = alarm.getDays();
        return alarm;
    }

    public void updateAlarm(Alarm alarm) {
        MainActivity.alarmDatabaseHandler.updateAlarm(alarm);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_mon:
                if (btn_mon.isChecked()) {
                    repeatDays[Calendar.MONDAY-1] = true;
                } else {
                    repeatDays[Calendar.MONDAY-1] = false;
                }
                break;
            case R.id.btn_tue:
                if (btn_tue.isChecked()) {
                    repeatDays[Calendar.TUESDAY-1] = true;
                } else {
                    repeatDays[Calendar.TUESDAY-1] = false;
                }
                break;
            case R.id.btn_wed:
                if (btn_wed.isChecked()) {
                    repeatDays[Calendar.WEDNESDAY-1] = true;
                } else {
                    repeatDays[Calendar.WEDNESDAY-1] = false;
                }
                break;
            case R.id.btn_thu:
                if (btn_thu.isChecked()) {
                    repeatDays[Calendar.THURSDAY-1] = true;
                } else {
                    repeatDays[Calendar.THURSDAY-1] = false;
                }
                break;
            case R.id.btn_fri:
                if (btn_fri.isChecked()) {
                    repeatDays[Calendar.FRIDAY-1] = true;
                } else {
                    repeatDays[Calendar.FRIDAY-1] = false;
                }
                break;
            case R.id.btn_sat:
                if (btn_sat.isChecked()) {
                    repeatDays[Calendar.SATURDAY-1] = true;
                } else {
                    repeatDays[Calendar.SATURDAY-1] = false;
                }
                break;
            case R.id.btn_sun:
                if (btn_sun.isChecked()) {
                    repeatDays[Calendar.SUNDAY-1] = true;
                } else {
                    repeatDays[Calendar.SUNDAY-1] = false;
                }
                break;
            case R.id.btn_save:
                alarm.setDays(repeatDays);
                alarm.setAlarm_name(alarm_name.getText().toString());
                if (alarm.checkSelectedDays()) {
                    updateAlarm(alarm);
                    AlarmAdapter.getAlarmList();
                    MainActivity.alarmAdapter.notifyDataSetChanged();
                    dismiss();
                    break;
                }
        }
    }

    private static String padding_str(int time) {
        if (time >= 10)
            return String.valueOf(time);
        else
            return "0" + String.valueOf(time);
    }
}
