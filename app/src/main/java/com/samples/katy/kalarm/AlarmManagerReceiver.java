package com.samples.katy.kalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Katy on 3/13/2015.
 */
public class AlarmManagerReceiver extends BroadcastReceiver {

    final public static String TIME = "time";


    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        AlarmDatabaseHandler alarmDatabaseHandler = new AlarmDatabaseHandler(context);
        List<Alarm> alarms = alarmDatabaseHandler.getAllAlarms();

        for (Alarm alarm : alarms) {
            if (alarm.getEnable()) { //is On
                PendingIntent pendingIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();
                int hours = Integer.parseInt(alarm.getAlarm_time().substring(0, 2));
                int minutes = Integer.parseInt(alarm.getAlarm_time().substring(3, 5));

                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 00);

                //Find next time to set
                final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);

                boolean alarmSet = false;

                //First check if it's later in the week
                for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                    if (alarm.getDays()[dayOfWeek - 1]
                            && dayOfWeek >= nowDay
                            && !(dayOfWeek == nowDay && hours < nowHour)
                            && !(dayOfWeek == nowDay && hours == nowHour && minutes <= nowMinute)) {
                        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                        setAlarm(context, calendar, pendingIntent);
                        alarmSet = true;
                        break;
                    }
                }

                //Else check if it's earlier in the week
                if (!alarmSet) {
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                        if (alarm.getDays()[dayOfWeek - 1]
                                && dayOfWeek <= nowDay
                                && alarm.getRepeat_weekly()) {
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);

                            setAlarm(context, calendar, pendingIntent);
                            alarmSet = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void setAlarm(Context context, Calendar calendar, PendingIntent pendingIntent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
 }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private static PendingIntent createPendingIntent(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(TIME, alarm.getAlarm_time());

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void cancelAlarms(Context context) {
        AlarmDatabaseHandler dbHelper = new AlarmDatabaseHandler(context);

        List<Alarm> alarms = dbHelper.getAllAlarms();

        if (alarms != null) {
            for (Alarm alarm : alarms) {
                if (alarm.getEnable()) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }
}
