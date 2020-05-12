package com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActivitiesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "datebase.db";
    private static final int SCHEMA = 3;

    public ActivitiesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public static class Tables {
        public static class ExerciseRecordTable {
            public static final String NAME = "activityrecord";
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_DATE = "date";
            public static final String COLUMN_TIME = "time";
            public static final String COLUMN_DISTANCE = "distance";
            public static final String COLUMN_COMMENT = "comment";
            public static final String COLUMN_ACTIVITY = "activity";
        }

        public static class WeightRecordTable {
            public static final String NAME = "weightrecord";
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_DATE = "date";
            public static final String COLUMN_WEIGHT = "weight";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.ExerciseRecordTable.NAME
                + " ("
                + Tables.ExerciseRecordTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Tables.ExerciseRecordTable.COLUMN_DATE + " TEXT,"
                + Tables.ExerciseRecordTable.COLUMN_TIME + " INTEGER,"
                + Tables.ExerciseRecordTable.COLUMN_DISTANCE + " REAL,"
                + Tables.ExerciseRecordTable.COLUMN_COMMENT + " TEXT,"
                + Tables.ExerciseRecordTable.COLUMN_ACTIVITY + " TEXT"
                + ");");
        db.execSQL("CREATE TABLE " + Tables.WeightRecordTable.NAME
                + " ("
                + Tables.WeightRecordTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Tables.WeightRecordTable.COLUMN_DATE + " TEXT,"
                + Tables.WeightRecordTable.COLUMN_WEIGHT + " REAL"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ExerciseRecordTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.WeightRecordTable.NAME);
        onCreate(db);
    }
}
