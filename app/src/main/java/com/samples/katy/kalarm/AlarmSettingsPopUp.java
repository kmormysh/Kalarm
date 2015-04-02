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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmSettingsPopUp extends DialogFragment implements AdapterView.OnItemClickListener,
            View.OnClickListener, TimePicker.OnTimeChangedListener {

    private String time;
    private boolean[] repeatDays = new boolean[7];
    private Button btn_create;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();
    private AlarmDatabaseHandler alarmDatabaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooseday, container);

        int tag = 0;
        List<View> subviews = ViewTreeHelper.getAllSubviews(view);
        for (int i = 0; i < subviews.size(); i++) {
            if (subviews.get(i) instanceof ToggleButton) {
                subviews.get(i).setOnClickListener(this);
                subviews.get(i).setTag(tag++);
                daysOfWeek.add((ToggleButton) subviews.get(i));
            }
        }

        btn_create = (Button) view.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(this);

        time = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());

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

        Object tag = v.getTag();
        if (tag instanceof Integer) {
            Integer index = (Integer) tag;
            repeatDays[index] = daysOfWeek.get(index).isChecked();
        } else if (v.getId() == R.id.btn_create) {
            Alarm newAlarm = new Alarm(time, "New Alarm", repeatDays, true, false);
            if (newAlarm.checkSelectedDays()) {
                setAlarm(newAlarm);
                dismiss();
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
