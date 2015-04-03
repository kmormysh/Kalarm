package com.samples.katy.kalarm;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class SetAlarmDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private Alarm alarm;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();
    private DialogCloseListener dialogCloseListener;
    private TimePicker timePicker;
    private EditText alarm_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooseday, container);

        int tag = 0;
        List<View> subviews = ViewTreeHelper.getAllSubviews(view);
        for (int i = 0; i < subviews.size(); i++) {
            if (subviews.get(i) instanceof ToggleButton) {
                subviews.get(i).setTag(tag++);
                daysOfWeek.add((ToggleButton) subviews.get(i));
            }
        }

        alarm_name = (EditText) view.findViewById(R.id.alarm_name);

        Button btn_create = (Button) view.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        if (alarm != null) {
            for (int i = 0; i < 7; i++) {
                daysOfWeek.get(i).setChecked(alarm.getDays()[i]);
            }
            alarm_name.setText(alarm.getAlarm_name());

            timePicker.setCurrentHour(Integer.parseInt(alarm.getAlarm_time().substring(0, 2)));
            timePicker.setCurrentMinute(Integer.parseInt(alarm.getAlarm_time().substring(3, 5)));
        }

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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_create) {
            boolean[] repeatDays = new boolean[7];
            for (int i = 0; i < daysOfWeek.size(); i++) {
                Object tag = daysOfWeek.get(i).getTag();
                if (tag instanceof Integer) {
                    Integer index = (Integer) tag;
                    repeatDays[index] = daysOfWeek.get(index).isChecked();
                }
            }
            if (this.dialogCloseListener != null) {
                if (alarm != null) {
                    this.dialogCloseListener.onCloseUpdate(alarm.getId(), repeatDays,
                            String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()),
                            alarm_name.getText().toString(), true);
                } else {
                    this.dialogCloseListener.onCloseCreate(repeatDays,
                            String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()),
                            alarm_name.getText().toString(), true);
                }
                dismiss();

            }
        }
    }

    public void setOnCloseListener(DialogCloseListener dialogCloseListener) {
        this.dialogCloseListener = dialogCloseListener;
    }

    public void openForCreate(FragmentManager fragmentManager,DialogCloseListener dialogCloseListener) {
        this.dialogCloseListener = dialogCloseListener;
        show(fragmentManager, "create");
    }

    public void openForEdit(FragmentManager fragmentManager, Alarm alarm, DialogCloseListener dialogCloseListener) {
        this.dialogCloseListener = dialogCloseListener;
        //load data from alarm to UI
        this.alarm = alarm;
        show(fragmentManager, "title for edit");
    }
}
