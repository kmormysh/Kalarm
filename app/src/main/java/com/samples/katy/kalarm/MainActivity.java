package com.samples.katy.kalarm;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends ActionBarActivity implements DialogInterface.OnDismissListener {

    private AlarmDatabaseHandler alarmDatabaseHandler;
    private AlarmManagerReceiver alarmManagerReceiver;
    private AlarmAdapter alarmAdapter;
    private ListView alarmList;
    private Button btn_setAlarm;
    private List<Alarm> alarms;
    private Activity mainActivity;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mainActivity = this;
        context = this;

        alarmDatabaseHandler = new AlarmDatabaseHandler(getBaseContext());
//        alarmDatabaseHandler.deleteAll();

        btn_setAlarm = (Button) findViewById(R.id.btn_setalarm);
        btn_setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmSettingsPopUp dialog = new AlarmSettingsPopUp();
                dialog.show(getFragmentManager(), "Choose day of the week");
                alarmList.invalidate();
            }

        });


        alarms = alarmDatabaseHandler.getAllAlarms();

        alarmAdapter = new AlarmAdapter(getBaseContext(), alarms);

        alarmList = (ListView) findViewById(R.id.alarm_list);
        registerForContextMenu(alarmList);

        alarmList.setAdapter(alarmAdapter);

        alarmManagerReceiver = new AlarmManagerReceiver();
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
        alarms = alarmDatabaseHandler.getAllAlarms();
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

            DialogFragment alarmDetails = AlarmDetails.newInstance(alarm.getId());
            alarmDetails.show(ft, "dialog");
        }
        if (menuItemName.equals(menuItems[1])) { //Delete
            alarmManagerReceiver.cancelAlarm(context);
            alarmDatabaseHandler.deleteAlarm(alarm);
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
    public void onDismiss(DialogInterface dialog) {
        alarmAdapter.getAlarmList();
        alarmAdapter.notifyDataSetChanged();
    }
}
