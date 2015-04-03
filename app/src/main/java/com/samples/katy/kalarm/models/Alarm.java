package com.samples.katy.kalarm.models;

public class Alarm {

    private int alarmId;
    private String alarmTime;
    private String alarmName;
    private String alarmDays;
    private boolean isRepeatedWeekly;
    private boolean isEnabled;
    private boolean[] days = new boolean[7];

    public Alarm() {
    }

    // create custom alarm
    public Alarm(String alarmTime, String alarmName, boolean[] days,
                 boolean isRepeatedWeekly, boolean isEnabled) {
        this.alarmTime = alarmTime;
        this.alarmName = alarmName;
        this.days = days;
        this.alarmDays = parseDaysIntToString(days);
        this.isRepeatedWeekly = isRepeatedWeekly;
        this.isEnabled = isEnabled;
    }

    // read from DB
    public Alarm(int alarmId, String alarmTime, String alarmName, String alarmDays,
                 boolean isRepeatedWeekly, boolean isEnabled) {
        this(alarmTime, alarmName, new boolean[7], isRepeatedWeekly, isEnabled);
        this.alarmId = alarmId;
        this.alarmDays = alarmDays;
        this.days = parseDaysStringToInt(alarmDays);
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String time) {
        this.alarmTime = time;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmDays() {
        return alarmDays;
    }

    public void setAlarmDays(String alarm_day) {
        this.alarmDays = alarm_day;
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
        setAlarmDays(parseDaysIntToString(days));
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

    public boolean isAtLeastOneDaySelected(){
        for (int i = 0; i < 7; i++)
            if (days[i])
                return true;
        return false;
    }

    public int getHours(){
        return Integer.parseInt((getAlarmTime().split(":"))[0]);
    }

    public int getMinutes(){
        return Integer.parseInt((getAlarmTime().split(":"))[1]);
    }

}
