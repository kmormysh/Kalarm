package com.samples.katy.kalarm;

import java.util.Calendar;

/**
 * Created by Katy on 3/17/2015.
 */

public class Alarm {

    private int id;
    private String alarm_time;
    private String alarm_name;
    private String alarm_days;
    private boolean repeat_weekly;
    private boolean enable;
    private boolean[] days = new boolean[7];

    public static final String ID = "id";

    public Alarm() {
    }

    // create custom alarm
    public Alarm(String alarm_time, String alarm_name, boolean[] days,
                 boolean repeat_weekly, boolean enable) {
        this.alarm_time = alarm_time;
        this.alarm_name = alarm_name;
        this.days = days;
        this.alarm_days = parseDaysIntToString(days);
        this.repeat_weekly = repeat_weekly;
        this.enable = enable;
    }

    // read from DB
    public Alarm(int id, String alarm_time, String alarm_name, String alarm_days,
                 boolean repeat_weekly, boolean enable) {
        this(alarm_time, alarm_name, new boolean[7], repeat_weekly, enable);
        this.id = id;
        this.alarm_days = alarm_days;
        this.days = parseDaysStringToInt(alarm_days);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getRepeat_weekly() {
        return repeat_weekly;
    }

    public void setRepeat_weekly(boolean repeat_weekly) {
        this.repeat_weekly = repeat_weekly;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
        setAlarm_day(parseDaysIntToString(days));
    }

    public static String parseDaysIntToString(boolean[] days) {
        String parsedDays = "";
        if (days[0])
            parsedDays += "MON ";
        if (days[1])
            parsedDays += "TUE ";
        if (days[2])
            parsedDays += "WED ";
        if (days[3])
            parsedDays += "THU ";
        if (days[4])
            parsedDays += "FRI ";
        if (days[5])
            parsedDays += "SAT ";
        if (days[6])
            parsedDays += "SUN ";

        return parsedDays;
    }

    public static boolean[] parseDaysStringToInt(String days) {

        boolean[] parsedDays = new boolean[7];
        String[] repeatDays = days.split(" ");
        for (int i = 0; i < repeatDays.length; i++) {
            if (repeatDays[i].equals("MON")) {
                parsedDays[0] = true;
                continue;
            }
            if (repeatDays[i].equals("TUE")){
                parsedDays[1] = true;
                continue;
            }
            if (repeatDays[i].equals("WED")){
                parsedDays[2] = true;
                continue;
            }
            if (repeatDays[i].equals("THU")){
                parsedDays[3] = true;
                continue;
            }
            if (repeatDays[i].equals("FRI")){
                parsedDays[4] = true;
                continue;
            }
            if (repeatDays[i].equals("SAT")){
                parsedDays[5] = true;
                continue;
            }
            if (repeatDays[i].equals("SUN")){
                parsedDays[6] = true;
                continue;
            }
        }

        return parsedDays;
    }

    public boolean checkSelectedDays(){
        for (int i = 0; i < 7; i++)
            if (days[i])
                return true;
        return false;
    }
}
