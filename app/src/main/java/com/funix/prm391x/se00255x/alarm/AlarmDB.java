package com.funix.prm391x.se00255x.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class AlarmDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "database";
    static final String TABLE_ALARM = "alarm";
    // table columns
    static final String REQUEST_CODE = "_requestCode";
    static final String TIME_IN_MILLIS = "timeInMillis";
    // MainActivity context
    private Context mCtx;

    AlarmDB(Context context) {
        super(context, DB_NAME, null, 1);
        mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ALARM + " (" + REQUEST_CODE + " CHAR(9) PRIMARY KEY, "
                + TIME_IN_MILLIS + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }

    long insert(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_CODE, alarm.getRequestCode());
        values.put(TIME_IN_MILLIS, alarm.getTimeInMillis());
        return db.insert(TABLE_ALARM, null, values);
    }

    void update(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME_IN_MILLIS, alarm.getTimeInMillis());
        db.update(TABLE_ALARM, values, REQUEST_CODE + "=?", new String[]{"" + alarm.getRequestCode()});
    }

    void delete(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ALARM, REQUEST_CODE + "=?", new String[]{"" + alarm.getRequestCode()});
    }

    ArrayList<Alarm> load() {
        ArrayList<Alarm> listOfAlarms = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARM, null);
        while (cursor.moveToNext()) {
            int requestCode = cursor.getInt(cursor.getColumnIndex(REQUEST_CODE));
            long timeInMillis = cursor.getLong(cursor.getColumnIndex(TIME_IN_MILLIS));
            listOfAlarms.add(new Alarm(mCtx, requestCode, timeInMillis, true));
        }
        cursor.close();
        return listOfAlarms;
    }
}
