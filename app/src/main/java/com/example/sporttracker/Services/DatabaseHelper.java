package com.example.sporttracker.Services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "datebase.db";
    private static final int SCHEMA = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public static class Tables {
        public static class ActivityRecordTable {
            public static final String NAME = "activityrecord";
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_DATE = "date";
            public static final String COLUMN_TIME = "time";
            public static final String COLUMN_DISTANCE = "distance";
            public static final String COLUMN_COMMENT = "comment";
            public static final String COLUMN_ACTIVITY = "activity";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.ActivityRecordTable.NAME
                + " ("
                + Tables.ActivityRecordTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Tables.ActivityRecordTable.COLUMN_DATE + " TEXT,"
                + Tables.ActivityRecordTable.COLUMN_TIME + " INTEGER,"
                + Tables.ActivityRecordTable.COLUMN_DISTANCE + " REAL,"
                + Tables.ActivityRecordTable.COLUMN_COMMENT + " TEXT,"
                + Tables.ActivityRecordTable.COLUMN_ACTIVITY + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ActivityRecordTable.NAME);
        onCreate(db);
    }
}
