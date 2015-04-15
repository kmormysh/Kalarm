package com.samples.katy.kalarm.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.samples.katy.kalarm.models.pojo.Alarm;
import com.samples.katy.kalarm.adapters.AlarmAdapter;
import com.samples.katy.kalarm.utils.AlarmsRepository;
import com.samples.katy.kalarm.models.AlarmManager;
import com.samples.katy.kalarm.dialogfragments.AlarmSetupDialogFragment;
import com.samples.katy.kalarm.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements AlarmSetupDialogFragment.DialogCloseListener, AlarmsRepository.DeleteCallback, AlarmsRepository.GetAlarmsCallback, AlarmsRepository.CreateAlarmCallback {

    private AlarmsRepository alarmsRepository;
    private AlarmManager alarmManager;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarms;

    @InjectView(R.id.txt_no_alarms)
    TextView noAlarms;
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

        alarmsRepository.getAllAlarms(false, this);
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
        alarmsRepository.getAllAlarms(false, this);
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
            alarmsRepository.deleteAlarm(alarm, this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_settings) {
            intent = new Intent(getBaseContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCloseCreate(boolean[] days, int hours, int minutes, String name, boolean isRepeat) {
        Alarm newAlarm = new Alarm(0, hours, minutes, name, days, isRepeat, false);
        if (newAlarm.isAtLeastOneDaySelected()) {
            alarmsRepository.addAlarm(newAlarm, this);
            alarmAdapter.getAlarmList();
            alarmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCloseUpdate(int alarm_id, boolean[] days, int hours, int minutes, String name, boolean isRepeat) {
        Alarm updateAlarm = new Alarm(alarm_id, hours, minutes, name, days, isRepeat, false);
//        alarmsRepository.getAlarm(alarm_id, this);
        if (updateAlarm.isAtLeastOneDaySelected()) {
            alarmsRepository.updateAlarm(updateAlarm);
            alarmAdapter.getAlarmList();
            alarmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGotAllAlarms(List<Alarm> alarm) {
        this.alarms = alarm;
        setTextViewVisibility();
    }

    @Override
    public void onGotAlarm(Alarm alarm) {

    }

    @Override
    public void onDelete() {
        setTextViewVisibility();
    }

    @Override
    public void onCreate() {
        setTextViewVisibility();
    }

    private void setTextViewVisibility(){
        if (alarms.size() > 0) {
            noAlarms.setVisibility(View.GONE);
        } else {
            noAlarms.setVisibility(View.VISIBLE);
        }
    }
}
