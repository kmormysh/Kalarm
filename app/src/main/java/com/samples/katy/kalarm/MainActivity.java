package com.samples.katy.kalarm;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static AlarmDatabaseHandler alarmDatabaseHandler;
    public static AlarmManagerReceiver alarmManagerReceiver;
    public static AlarmAdapter alarmAdapter;
    private ListView alarmList;
    public static List<Alarm> alarms;
    public static Activity mainActivity;
    public static Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        context = this;

        alarmDatabaseHandler = new AlarmDatabaseHandler(getBaseContext());
//        alarmDatabaseHandler.deleteAll();

        Button btn_setAlarm = (Button) findViewById(R.id.btn_setalarm);
        btn_setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmSettingsPopUp dialog = new AlarmSettingsPopUp();
                dialog.show(getFragmentManager(), "Choose day of the week");
            }
        });

        alarms = alarmDatabaseHandler.getAllAlarms();

        alarmAdapter = new AlarmAdapter(getBaseContext(), alarms);
        alarmList = (ListView) findViewById(R.id.alarm_list);
        alarmList.setAdapter(alarmAdapter);

        alarmManagerReceiver = new AlarmManagerReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void cancelRepeatingTimer(View view){

        Switch alarmSwitch = (Switch) view.findViewById(R.id.on_off_switch);
        Context context = this.getApplicationContext();
        if (alarmSwitch.isChecked()) { //On
            alarmManagerReceiver.setAlarms(context);
        }
        else { //Off

            Alarm alarm = alarmDatabaseHandler.getAlarm(1);

            alarm.setEnable(0);
            alarmDatabaseHandler.updateAlarm(alarm);

            alarmManagerReceiver.cancelAlarm(context);
            Toast.makeText(context, "Alarm is off", Toast.LENGTH_SHORT).show();
        }
    }

}
