package com.samples.katy.kalarm;

import java.util.List;

/**
 * Created by Katy on 3/5/2015.
 */
public interface IDatabaseHandler {
    public void addAlarm (Alarm alarm);
    public Alarm getAlarm(int id);
    public List<Alarm> getAllAlarms();
    public int getAlarmCount();
    public int updateAlarm(Alarm alarm);
    public void deleteAlarm(Alarm alarm);
    public void deleteAll();
}
