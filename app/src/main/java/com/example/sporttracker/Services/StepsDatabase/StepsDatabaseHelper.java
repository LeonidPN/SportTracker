package com.example.sporttracker.Services.StepsDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StepsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stepsdatebase.db";
    private static final int SCHEMA = 1;

    public StepsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public static class Tables {
        public static class StepsRecordTable {
            public static final String NAME = "stepsrecord";
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_DATE = "date";
            public static final String COLUMN_HOUR_IN_DAY = "hourinday";
            public static final String COLUMN_COUNT = "count";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.StepsRecordTable.NAME
                + " ("
                + Tables.StepsRecordTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Tables.StepsRecordTable.COLUMN_DATE + " TEXT,"
                + Tables.StepsRecordTable.COLUMN_HOUR_IN_DAY + " INTEGER,"
                + Tables.StepsRecordTable.COLUMN_COUNT + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.StepsRecordTable.NAME);
        onCreate(db);
    }
}
