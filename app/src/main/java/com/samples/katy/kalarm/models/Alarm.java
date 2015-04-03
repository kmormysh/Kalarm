package com.samples.katy.kalarm.models;

public class Alarm {

    private int alarmId;
    private String alarmName;
    private int alarmMinutes;
    private int alarmHours;
    private boolean isRepeatedWeekly;
    private boolean isEnabled;
    private boolean[] days = new boolean[DAYS_PER_WEEK];
    public static final int DAYS_PER_WEEK = 7;

    public Alarm() { }

    public Alarm(int alarmId, int hours, int minutes, String alarmName, boolean[] alarmDays,
                 boolean isRepeatedWeekly, boolean isEnabled) {
        this.alarmHours = hours;
        this.alarmMinutes = minutes;
        this.alarmName = alarmName;
        this.days = alarmDays;
        this.isRepeatedWeekly = isRepeatedWeekly;
        this.isEnabled = isEnabled;
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public int getId() {
        return alarmId;
    }

    public void setId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean getRepeatedWeekly() {
        return isRepeatedWeekly;
    }

    public void setRepeatedWeekly(boolean repeatedWeekly) {
        this.isRepeatedWeekly = repeatedWeekly;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    public boolean isAtLeastOneDaySelected() {
        for (int i = 0; i < getDays().length; i++)
            if (days[i])
                return true;
        return false;
    }

    public int getAlarmHours() {
        return alarmHours;
    }

    public void setAlarmHours(int alarmHours) {
        this.alarmHours = alarmHours;
    }

    public int getAlarmMinutes() {
        return alarmMinutes;
    }

    public void setAlarmMinutes(int alarmMinutes) {
        this.alarmMinutes = alarmMinutes;
    }
}
