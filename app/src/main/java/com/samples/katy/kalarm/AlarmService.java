package com.samples.katy.kalarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Katy on 3/30/2015.
 */
public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), AlarmPopUpScreen.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmManagerReceiver.setAlarms(this);

        return super.onStartCommand(intent, flags, startId);
    }
}
