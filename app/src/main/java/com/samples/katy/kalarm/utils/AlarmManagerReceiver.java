package com.samples.katy.kalarm.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.samples.katy.kalarm.models.Alarm;

import java.util.Calendar;
import java.util.List;

public class AlarmManagerReceiver extends BroadcastReceiver {

    final public static String HOURS = "hours";
    final public static String MINUTES = "minutes";

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        AlarmDatabaseHandler alarmDatabaseHandler = new AlarmDatabaseHandler(context);
        List<Alarm> alarms = alarmDatabaseHandler.getAllAlarms();

        for (Alarm alarm : alarms) {
            if (alarm.getIsEnabled()) { //is On
                PendingIntent pendingIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();
                int hours = alarm.getAlarmHours();
                int minutes = alarm.getAlarmMinutes();

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
                                && alarm.getRepeatedWeekly()) {
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
        intent.putExtra(HOURS, alarm.getAlarmHours());
        intent.putExtra(MINUTES, alarm.getAlarmMinutes());

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void cancelAlarms(Context context) {
        AlarmDatabaseHandler dbHelper = new AlarmDatabaseHandler(context);

        List<Alarm> alarms = dbHelper.getAllAlarms();

        if (alarms != null) {
            for (Alarm alarm : alarms) {
                if (alarm.getIsEnabled()) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }
}
