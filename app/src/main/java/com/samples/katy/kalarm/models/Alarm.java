package com.samples.katy.kalarm.models;

public class Alarm {

    private int alarmId;
    private String alarmName;
    private String alarmDays;
    private int alarmMinutes;
    private int alarmHours;
    private boolean isRepeatedWeekly;
    private boolean isEnabled;
    private boolean[] days = new boolean[7];

    public Alarm() {
    }

    // create custom alarm
    public Alarm(int hours, int minutes, String alarmName, boolean[] days,
                 boolean isRepeatedWeekly, boolean isEnabled) {
        this.alarmHours = hours;
        this.alarmMinutes = minutes;
        this.alarmName = alarmName;
        this.days = days;
        this.alarmDays = convertDaysIntToString(days);
        this.isRepeatedWeekly = isRepeatedWeekly;
        this.isEnabled = isEnabled;
    }

    // read from DB
    public Alarm(int alarmId, int hours, int minutes, String alarmName, boolean[] alarmDays,
                 boolean isRepeatedWeekly, boolean isEnabled) {
        this(hours, minutes, alarmName, alarmDays, isRepeatedWeekly, isEnabled);
        this.alarmId = alarmId;
        this.alarmDays = convertDaysIntToString(alarmDays);
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

    public boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    public static String convertDaysIntToString(boolean[] days) {
        String parsedDays = "";
        for (int i = 0; i < days.length; i++) {
            if (days[i]) {
                parsedDays += i + " ";
            }
        }

        return parsedDays;
    }

    public static boolean[] convertDaysStringToInt(String days) {

        boolean[] parsedDays = new boolean[7];
        String[] repeatDays = days.split(" ");
        for (int i = 0; i < repeatDays.length; i++) {
            int index = Integer.parseInt(repeatDays[i]);
            parsedDays[index] = true;
        }

        return parsedDays;
    }

    public boolean isAtLeastOneDaySelected() {
        for (int i = 0; i < 7; i++)
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
