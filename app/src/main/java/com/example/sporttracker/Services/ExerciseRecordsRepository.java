package com.example.sporttracker.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sporttracker.Models.ActivityRecordModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRecordsRepository extends AbstractRepository {

    private SimpleDateFormat formatForDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public ExerciseRecordsRepository(Context context) {
        super(context);
        table = DatabaseHelper.Tables.ActivityRecordTable.NAME;
        columns = new String[]{DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ID,
                DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DATE,
                DatabaseHelper.Tables.ActivityRecordTable.COLUMN_TIME,
                DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DISTANCE,
                DatabaseHelper.Tables.ActivityRecordTable.COLUMN_COMMENT,
                DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ACTIVITY};
    }

    @Override
    public List getList() {
        ArrayList<ActivityRecordModel> elements = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DATE));
                int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_TIME));
                float distance = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DISTANCE));
                String comment = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_COMMENT));
                String activity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ACTIVITY));

                ActivityRecordModel element = new ActivityRecordModel();
                element.setId(id);
                element.setActivity(activity);
                element.setComment(comment);
                try {
                    element.setDate(formatForDate.parse(date));
                } catch (ParseException e) {
                }
                element.setTime(time);
                element.setDistance(distance);

                elements.add(element);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return elements;
    }

    @Override
    public Object getElement(int id) {
        ActivityRecordModel element = new ActivityRecordModel();
        String query = String.format("SELECT * FROM %s WHERE %s=?", table, DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DATE));
            int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_TIME));
            float distance = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DISTANCE));
            String comment = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_COMMENT));
            String activity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ACTIVITY));

            element.setId(id);
            element.setActivity(activity);
            element.setComment(comment);
            try {
                element.setDate(formatForDate.parse(date));
            } catch (ParseException e) {
            }
            element.setTime(time);
            element.setDistance(distance);
        }
        cursor.close();
        return element;
    }

    @Override
    public int update(Object element) {
        String whereClause = DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ID + "="
                + ((ActivityRecordModel) element).getId();
        ContentValues cv = getContentValues(element);
        return database.update(table, cv, whereClause, null);
    }

    @Override
    protected ContentValues getContentValues(Object element) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DATE, formatForDate.format(((ActivityRecordModel) element).getDate()));
        cv.put(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_TIME, ((ActivityRecordModel) element).getTime());
        cv.put(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_DISTANCE, ((ActivityRecordModel) element).getDistance());
        cv.put(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_COMMENT, ((ActivityRecordModel) element).getComment());
        cv.put(DatabaseHelper.Tables.ActivityRecordTable.COLUMN_ACTIVITY, ((ActivityRecordModel) element).getActivity());

        return cv;
    }
}
