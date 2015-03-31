package com.samples.katy.kalarm;

import java.util.Calendar;

/**
 * Created by Katy on 3/17/2015.
 */

public class Alarm {

    private int keyID;
    private String alarm_time;
    private String alarm_name;
    private String alarm_days;
    private int repeat_weekly;
    private int enable;
    private boolean[] days = new boolean[7];

    public Alarm() { }

    // create custom alarm
    public Alarm(String alarm_time, String alarm_name, boolean[] days,
                 int repeat_weekly, int enable) {
        this.alarm_time = alarm_time;
        this.alarm_name = alarm_name;
        this.days = days;
        this.alarm_days = parseDaysIntToString(days);
        this.repeat_weekly = repeat_weekly;
        this.enable = enable;
    }

    // read from DB
    public Alarm(int keyID, String alarm_time, String alarm_name, String alarm_days,
                 int repeat_weekly, int enable) {
        this.keyID = keyID;
        this.alarm_time = alarm_time;
        this.alarm_name = alarm_name;
        this.alarm_days = alarm_days;
        this.days = parseDaysStringToInt(alarm_days);
        this.repeat_weekly = repeat_weekly;
        this.enable = enable;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String time) {
        this.alarm_time = time;
    }

    public String getAlarm_name() {
        return alarm_name;
    }

    public void setAlarm_name(String alarm_name) {
        this.alarm_name = alarm_name;
    }

    public String getAlarm_day() {
        return alarm_days;
    }

    public void setAlarm_day(String alarm_day) {
        this.alarm_days = alarm_day;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public int getRepeat_weekly() {
        return repeat_weekly;
    }

    public void setRepeat_weekly(int repeat_weekly) {
        this.repeat_weekly = repeat_weekly;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
        setAlarm_day(parseDaysIntToString(days));
    }

    public String parseDaysIntToString (boolean[] days) {
        String parsedDays = "";
        if (days[Calendar.MONDAY-1])
            parsedDays += "MON ";
        if (days[Calendar.TUESDAY-1])
            parsedDays += "TUE ";
        if (days[Calendar.WEDNESDAY-1])
            parsedDays += "WED ";
        if (days[Calendar.THURSDAY-1])
            parsedDays += "THU ";
        if (days[Calendar.FRIDAY-1])
            parsedDays += "FRI ";
        if (days[Calendar.SATURDAY-1])
            parsedDays += "SAT ";
        if (days[Calendar.SUNDAY-1])
            parsedDays += "SUN";

        return parsedDays;
    }

    public boolean[] parseDaysStringToInt (String days) {

        boolean[] parsedDays = new boolean[7];
        String[] repeatDays = days.split(",");
        for (int i = 0; i < repeatDays.length; i++){
            switch (repeatDays[i]) {
                case "MON ":
                    parsedDays[Calendar.MONDAY-1] = true;
                    break;
                case "TUE ":
                    parsedDays[Calendar.TUESDAY-1] = true;
                    break;
                case "WED ":
                    parsedDays[Calendar.WEDNESDAY-1] = true;
                    break;
                case "THU ":
                    parsedDays[Calendar.THURSDAY-1] = true;
                    break;
                case "FRI ":
                    parsedDays[Calendar.FRIDAY-1] = true;
                    break;
                case "SAT ":
                    parsedDays[Calendar.SATURDAY-1] = true;
                    break;
                case "SUN ":
                    parsedDays[Calendar.SUNDAY-1] = true;
                    break;
            }
        }

        return parsedDays;
    }
}
