package com.samples.katy.kalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katy on 3/5/2015.
 */
public class AlarmDatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarmManager";
    private static final String TABLE_ALARMS = "alarms";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DAY = "days";
    private static final String KET_TIME = "time";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_ENABLE = "enable";

    public AlarmDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean recordsExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_ALARMS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToNext();
    }


    @Override
    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getAlarm_name());
        values.put(KEY_DAY, alarm.getAlarm_day());
        values.put(KET_TIME, alarm.getAlarm_time());
        values.put(KEY_REPEAT, alarm.getRepeat_weekly() ? 1:0);
        values.put(KEY_ENABLE, alarm.getEnable() ? 1:0);

        db.insert(TABLE_ALARMS, null, values);

        db.close();
    }

    @Override
    public Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[]{KEY_ID, KEY_NAME, KEY_DAY, KET_TIME,
                                                            KEY_REPEAT, KEY_ENABLE},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Alarm alarm = new Alarm(cursor.getInt(0), cursor.getString(3), cursor.getString(1),
                                cursor.getString(2), cursor.getInt(4)>0, cursor.getInt(5)>0);

        alarm.setDays(alarm.parseDaysStringToInt(alarm.getAlarm_day()));

        return alarm;
    }

    @Override
    public List<Alarm> getAllAlarms() {
        List<Alarm> alarmList = new ArrayList<Alarm>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setAlarm_name(cursor.getString(1));
                alarm.setAlarm_day(cursor.getString(2));
                alarm.setAlarm_time(cursor.getString(3));
                alarm.setRepeat_weekly(cursor.getInt(4)>0);
                alarm.setEnable(cursor.getInt(5)>0);

                alarm.setDays(alarm.parseDaysStringToInt(alarm.getAlarm_day()));

                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }

        return alarmList;
    }

    @Override
    public int getAlarmCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    @Override
    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, alarm.getId());
        values.put(KEY_NAME, alarm.getAlarm_name());
        values.put(KEY_DAY, alarm.parseDaysIntToString(alarm.getDays()));
        values.put(KET_TIME, alarm.getAlarm_time());
        values.put(KEY_REPEAT, alarm.getRepeat_weekly() ? 1:0);
        values.put(KEY_ENABLE, alarm.getEnable() ? 1:0);

        return db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?", new String[]{String.valueOf(alarm.getId())});

        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, null, null);

        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CALLS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DAY + " INTEGER," + KET_TIME + " TEXT,"
                + KEY_REPEAT + " INTEGER," + KEY_ENABLE + " INTEGER" + ")";
        db.execSQL(CREATE_CALLS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }
}
