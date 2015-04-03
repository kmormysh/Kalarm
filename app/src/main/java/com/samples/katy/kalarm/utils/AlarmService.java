package com.samples.katy.kalarm.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.samples.katy.kalarm.activities.AlarmWakeUpActivity;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), AlarmWakeUpActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmManager.rescheduleAlarms(this);

        return super.onStartCommand(intent, flags, startId);
    }
}
