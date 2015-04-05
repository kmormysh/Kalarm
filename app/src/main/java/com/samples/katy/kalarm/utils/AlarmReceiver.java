package com.samples.katy.kalarm.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.samples.katy.kalarm.models.AlarmManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager.rescheduleAlarms(context);
    }
}
