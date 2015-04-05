package com.samples.katy.kalarm.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.samples.katy.kalarm.models.POJO.Alarm;
import com.samples.katy.kalarm.adapters.AlarmAdapter;
import com.samples.katy.kalarm.utils.AlarmsRepository;
import com.samples.katy.kalarm.models.AlarmManager;
import com.samples.katy.kalarm.dialogfragments.AlarmSetupDialogFragment;
import com.samples.katy.kalarm.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements AlarmSetupDialogFragment.DialogCloseListener {

    private AlarmsRepository alarmsRepository;
    private AlarmManager alarmManager;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarms;

    @InjectView(R.id.alarm_list) ListView alarmList;
    @OnClick(R.id.btn_setalarm) void setAlarm() {
        AlarmSetupDialogFragment dialog = new AlarmSetupDialogFragment();
        dialog.openForCreate(getFragmentManager(), MainActivity.this);
        alarmList.invalidate();
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        alarmsRepository = new AlarmsRepository(getBaseContext());
        alarmManager = new AlarmManager(alarmsRepository);

        alarms = alarmsRepository.getAllAlarms(false);
        alarmAdapter = new AlarmAdapter(getBaseContext(), alarms,
                alarmsRepository, alarmManager);

        registerForContextMenu(alarmList);
        alarmList.setAdapter(alarmAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.alarm_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        alarms = alarmsRepository.getAllAlarms(false);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        Alarm alarm = alarms.get(info.position);

        if (menuItemName.equals(menuItems[0])) {//Details
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            AlarmSetupDialogFragment alarmSetupDialogFragment = new AlarmSetupDialogFragment();
            alarmSetupDialogFragment.openForEdit(getFragmentManager(), alarm, this);
        }
        if (menuItemName.equals(menuItems[1])) { //Delete
            alarmManager.cancelAlarm(this);
            alarmsRepository.deleteAlarm(alarm);
            alarmAdapter.getAlarmList();
            alarmAdapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCloseCreate(boolean[] days, int hours, int minutes, String name, boolean isRepeat) {
        Alarm newAlarm = new Alarm(0, hours, minutes, name, days, isRepeat, false);
        if (newAlarm.isAtLeastOneDaySelected()) {
            alarmsRepository.addAlarm(newAlarm);
            alarmAdapter.getAlarmList();
            alarmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCloseUpdate(int alarm_id, boolean[] days, int hours, int minutes, String name, boolean isRepeat) {
        Alarm updateAlarm = alarmsRepository.getAlarm(alarm_id);
        updateAlarm.setAlarmName(name);
        updateAlarm.setAlarmHours(hours);
        updateAlarm.setAlarmMinutes(minutes);
        updateAlarm.setDays(days);
        updateAlarm.setRepeatedWeekly(isRepeat);
        if (updateAlarm.isAtLeastOneDaySelected()) {
            alarmsRepository.updateAlarm(updateAlarm);
            alarmAdapter.getAlarmList();
            alarmAdapter.notifyDataSetChanged();
        }
    }

}
