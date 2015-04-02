package com.samples.katy.kalarm;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmSettingsPopUp extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener, TimePicker.OnTimeChangedListener {

    private String time;
    private boolean[] repeatDays = new boolean[7];
    private Button btn_create;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();
    private AlarmDatabaseHandler alarmDatabaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooseday, container);

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

        btn_create = (Button) view.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(this);

        time = String.format("%02d", timePicker.getCurrentHour()) + ":" + String.format("%02d", timePicker.getCurrentMinute());

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
        alarmDatabaseHandler = new AlarmDatabaseHandler(getActivity().getBaseContext());
        alarmDatabaseHandler.addAlarm(alarm);
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
            case R.id.btn_create:
                Alarm newAlarm = new Alarm(time, "New Alarm", repeatDays, true, false);
                if (newAlarm.checkSelectedDays()) {
                    setAlarm(newAlarm);
                    dismiss();
                    break;
                }
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
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
