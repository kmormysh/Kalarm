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
    private EditText alarm_name;
    private Button btn_save;
    private AlarmDatabaseHandler alarmDatabaseHandler;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();

    private boolean[] repeatDays = new boolean[7];

    static AlarmDetails newInstance(int id) {
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

        int tag = 0;
        List<View> subviews = ViewTreeHelper.getAllSubviews(view);
        for (int i = 0; i < subviews.size(); i++) {
            if (subviews.get(i) instanceof ToggleButton) {
                subviews.get(i).setOnClickListener(this);
                subviews.get(i).setTag(tag++);
                daysOfWeek.add((ToggleButton) subviews.get(i));
            }
        }

        for (int i = 0; i < 7; i++)
            daysOfWeek.get(i).setChecked(alarm.getDays()[i]);

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
                alarm.setAlarm_time(String.format("%02d:%02d", hourOfDay, minute));
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

        Object tag = v.getTag();
        if (tag instanceof Integer) {
            Integer index = (Integer) tag;
            repeatDays[index] = daysOfWeek.get(index).isChecked();
        } else if (v.getId() == R.id.btn_save) {
            alarm.setDays(repeatDays);
            alarm.setAlarm_name(alarm_name.getText().toString());
            if (alarm.checkSelectedDays()) {
                updateAlarm(alarm);
                dismiss();
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
