package com.samples.katy.kalarm.dialogfragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.samples.katy.kalarm.models.Alarm;
import com.samples.katy.kalarm.R;
import com.samples.katy.kalarm.utils.ViewTreeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AlarmSetupDialogFragment extends DialogFragment {

    private Alarm alarm;
    private List<ToggleButton> daysOfWeek = new ArrayList<>();
    private DialogCloseListener dialogCloseListener;
    @InjectView(R.id.timePicker) TimePicker timePicker;
    @InjectView(R.id.alarm_name) EditText alarm_name;
    @InjectView(R.id.btn_create) Button btn_create;

    @OnClick(R.id.btn_create) void submit() {
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
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute(),
                        alarm_name.getText().toString(), true);
            } else {
                this.dialogCloseListener.onCloseCreate(repeatDays,
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute(),
                        alarm_name.getText().toString(), true);
            }
            dismiss();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooseday, container);
        ButterKnife.inject(this, view);

        int tag = 0;
        List<View> subviews = ViewTreeHelper.getAllSubviews(view);
        for (int i = 0; i < subviews.size(); i++) {
            if (subviews.get(i) instanceof ToggleButton) {
                subviews.get(i).setTag(tag++);
                daysOfWeek.add((ToggleButton) subviews.get(i));
            }
        }

        if (alarm != null) {
            for (int i = 0; i < daysOfWeek.size(); i++) {
                daysOfWeek.get(i).setChecked(alarm.getDays()[i]);
            }
            alarm_name.setText(alarm.getAlarmName());

            timePicker.setCurrentHour(alarm.getAlarmHours());
            timePicker.setCurrentMinute(alarm.getAlarmMinutes());

            btn_create.setText("Save");
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void openForCreate(FragmentManager fragmentManager, DialogCloseListener dialogCloseListener) {
        this.dialogCloseListener = dialogCloseListener;
        show(fragmentManager, "Create new alarm");
    }

    public void openForEdit(FragmentManager fragmentManager, Alarm alarm, DialogCloseListener dialogCloseListener) {
        this.dialogCloseListener = dialogCloseListener;
        //load data from alarm to UI
        this.alarm = alarm;
        show(fragmentManager, "Edit current alarm");
    }

    public interface DialogCloseListener {

        public void onCloseCreate(boolean[] days, int hours, int minutes, String name, boolean isRepeat);

        public void onCloseUpdate(int alarm_id, boolean[] days, int hours, int minutes, String name, boolean isRepeat);
    }
}
