package com.samples.katy.kalarm;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmDetails extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Alarm alarm;
    EditText alarm_name;
    Button btn_save;
    private AlarmDatabaseHandler alarmDatabaseHandler;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();

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

        alarmDatabaseHandler = new AlarmDatabaseHandler(getActivity().getBaseContext());
        getAlarm();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.alarm_details_pop_up, container);

        LinearLayout buttons = (LinearLayout) view.findViewById(R.id.btns_1);
        for (int i = 0; i < buttons.getChildCount(); i++){
            View v = buttons.getChildAt(i);
            if (v instanceof ToggleButton) {
                v.setOnClickListener(this);
                daysOfWeek.add((ToggleButton) v);
            }
        }

        buttons = (LinearLayout) view.findViewById(R.id.btns_2);
        for (int i = 0; i < buttons.getChildCount(); i++){
            View v = buttons.getChildAt(i);
            if (v instanceof ToggleButton) {
                v.setOnClickListener(this);
                daysOfWeek.add((ToggleButton) v);
            }
        }

        daysOfWeek.get(0).setChecked(alarm.getDays()[Calendar.MONDAY - 1]);
        daysOfWeek.get(1).setChecked(alarm.getDays()[Calendar.TUESDAY - 1]);
        daysOfWeek.get(2).setChecked(alarm.getDays()[Calendar.WEDNESDAY - 1]);
        daysOfWeek.get(3).setChecked(alarm.getDays()[Calendar.THURSDAY - 1]);
        daysOfWeek.get(4).setChecked(alarm.getDays()[Calendar.FRIDAY - 1]);
        daysOfWeek.get(5).setChecked(alarm.getDays()[Calendar.SATURDAY - 1]);
        daysOfWeek.get(6).setChecked(alarm.getDays()[Calendar.SUNDAY - 1]);

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
                alarm.setAlarm_time(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
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
        alarm = alarmDatabaseHandler.getAlarm(getArguments().getInt(Alarm.ID));
        repeatDays = alarm.getDays();
        return alarm;
    }

    public void updateAlarm(Alarm alarm) {
        alarmDatabaseHandler.updateAlarm(alarm);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_mon:
                repeatDays[Calendar.MONDAY - 1] = daysOfWeek.get(0).isChecked();
                break;
            case R.id.btn_tue:
                repeatDays[Calendar.TUESDAY - 1] = daysOfWeek.get(1).isChecked();
                break;
            case R.id.btn_wed:
                repeatDays[Calendar.WEDNESDAY - 1] = daysOfWeek.get(2).isChecked();
                break;
            case R.id.btn_thu:
                repeatDays[Calendar.THURSDAY - 1] = daysOfWeek.get(3).isChecked();
                break;
            case R.id.btn_fri:
                repeatDays[Calendar.FRIDAY - 1] = daysOfWeek.get(4).isChecked();
                break;
            case R.id.btn_sat:
                repeatDays[Calendar.SATURDAY - 1] = daysOfWeek.get(5).isChecked();
                break;
            case R.id.btn_sun:
                repeatDays[Calendar.SUNDAY - 1] = daysOfWeek.get(6).isChecked();
                break;
            case R.id.btn_save:
                alarm.setDays(repeatDays);
                alarm.setAlarm_name(alarm_name.getText().toString());
                if (alarm.checkSelectedDays()) {
                    updateAlarm(alarm);
                    dismiss();
                    break;
                }
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
