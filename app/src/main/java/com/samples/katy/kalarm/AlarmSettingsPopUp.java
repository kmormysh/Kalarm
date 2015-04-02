package com.samples.katy.kalarm;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmSettingsPopUp extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener, TimePicker.OnTimeChangedListener {

    String days;
    String time;
    boolean[] repeatDays = new boolean[7];
    ToggleButton btn_mon;
    ToggleButton btn_tue;
    ToggleButton btn_wed;
    ToggleButton btn_thu;
    ToggleButton btn_fri;
    ToggleButton btn_sat;
    ToggleButton btn_sun;
    Button btn_create;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooseday, container);

        btn_mon = (ToggleButton) view.findViewById(R.id.btn_mon);
        btn_mon.setOnClickListener(this);
        btn_tue = (ToggleButton) view.findViewById(R.id.btn_tue);
        btn_tue.setOnClickListener(this);
        btn_wed = (ToggleButton) view.findViewById(R.id.btn_wed);
        btn_wed.setOnClickListener(this);
        btn_thu = (ToggleButton) view.findViewById(R.id.btn_thu);
        btn_thu.setOnClickListener(this);
        btn_fri = (ToggleButton) view.findViewById(R.id.btn_fri);
        btn_fri.setOnClickListener(this);
        btn_sat = (ToggleButton) view.findViewById(R.id.btn_sat);
        btn_sat.setOnClickListener(this);
        btn_sun = (ToggleButton) view.findViewById(R.id.btn_sun);
        btn_sun.setOnClickListener(this);

        days = "";

        btn_create = (Button) view.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(this);

        time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();

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

    public void setAlarm(Alarm alarm) {
        MainActivity.alarmDatabaseHandler.addAlarm(alarm);
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
            case R.id.btn_create:
                Alarm newAlarm = new Alarm(time, "New Alarm", repeatDays, true, false);
                setAlarm(newAlarm);
                AlarmAdapter.getAlarmList();
                MainActivity.alarmAdapter.notifyDataSetChanged();
                dismiss();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        time = padding_str(hourOfDay) + ":" + padding_str(minute);
    }

    private static String padding_str(int time) {
        if (time >= 10)
            return String.valueOf(time);
        else
            return "0" + String.valueOf(time);
    }
}
