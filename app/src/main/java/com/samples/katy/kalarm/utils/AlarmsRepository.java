package com.samples.katy.kalarm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.samples.katy.kalarm.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmsRepository extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarmManager";
    private static final String TABLE_ALARMS = "alarms";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DAY = "days";
    private static final String KEY_HOURS = "hours";
    private static final String KET_MINUTES = "minutes";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_ENABLE = "enable";

    public AlarmsRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getAlarmName());
        values.put(KEY_DAY, convertDaysIntToString(alarm.getDays()));
        values.put(KEY_HOURS, alarm.getAlarmHours());
        values.put(KET_MINUTES, alarm.getAlarmMinutes());
        values.put(KEY_REPEAT, alarm.getRepeatedWeekly() ? 1 : 0);
        values.put(KEY_ENABLE, alarm.getIsEnabled() ? 1 : 0);

        db.insert(TABLE_ALARMS, null, values);

        db.close();
    }

    public Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] keys = {KEY_ID, KEY_NAME, KEY_DAY, KEY_HOURS, KET_MINUTES, KEY_REPEAT, KEY_ENABLE};

        Cursor cursor = db.query(TABLE_ALARMS,
                keys,
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        cursor.moveToFirst();


        Alarm alarm = new Alarm(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_HOURS)),
                cursor.getInt(cursor.getColumnIndex(KET_MINUTES)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                convertDaysStringToInt(cursor.getString(cursor.getColumnIndex(KEY_DAY))),
                cursor.getInt(cursor.getColumnIndex(KEY_REPEAT)) > 0,
                cursor.getInt(cursor.getColumnIndex(KEY_ENABLE)) > 0);


        cursor.close();

        return alarm;
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarmList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_HOURS)),
                        cursor.getInt(cursor.getColumnIndex(KET_MINUTES)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        convertDaysStringToInt(cursor.getString(cursor.getColumnIndex(KEY_DAY))),
                        cursor.getInt(cursor.getColumnIndex(KEY_REPEAT)) > 0,
                        cursor.getInt(cursor.getColumnIndex(KEY_ENABLE)) > 0);

                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return alarmList;
    }

    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, alarm.getId());
        values.put(KEY_NAME, alarm.getAlarmName());
        values.put(KEY_DAY, convertDaysIntToString(alarm.getDays()));
        values.put(KEY_HOURS, alarm.getAlarmHours());
        values.put(KET_MINUTES, alarm.getAlarmMinutes());
        values.put(KEY_REPEAT, alarm.getRepeatedWeekly() ? 1 : 0);
        values.put(KEY_ENABLE, alarm.getIsEnabled() ? 1 : 0);

        return db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});
    }

    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?", new String[]{String.valueOf(alarm.getId())});

        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, null, null);

        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CALLS_TABLE = "CREATE TABLE "
                + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DAY + " INTEGER,"
                + KEY_HOURS + " INTEGER,"
                + KET_MINUTES + " INTEGER,"
                + KEY_REPEAT + " INTEGER,"
                + KEY_ENABLE + " INTEGER" + ")";
        db.execSQL(CREATE_CALLS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    private boolean[] convertDaysStringToInt(String days) {

        boolean[] parsedDays = new boolean[Alarm.DAYS_PER_WEEK];
        String[] repeatDays = days.split(" ");
        for (int i = 0; i < repeatDays.length; i++) {
            int index = Integer.parseInt(repeatDays[i]);
            parsedDays[index] = true;
        }

        return parsedDays;
    }

    private String convertDaysIntToString(boolean[] days) {
        String parsedDays = "";
        for (int i = 0; i < days.length; i++) {
            if (days[i]) {
                parsedDays += i + " ";
            }
        }

        return parsedDays;
    }
}
