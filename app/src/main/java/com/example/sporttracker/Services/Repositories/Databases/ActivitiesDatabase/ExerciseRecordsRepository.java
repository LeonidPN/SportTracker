package com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.Services.Repositories.Databases.AbstractDatabaseRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRecordsRepository extends AbstractDatabaseRepository {

    public ExerciseRecordsRepository(Context context) {
        super(context);
        dbHelper = new ActivitiesDatabaseHelper(context);
        table = ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.NAME;
        columns = new String[]{ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ID,
                ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DATE,
                ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_TIME,
                ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DISTANCE,
                ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_COMMENT,
                ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ACTIVITY};
    }

    @Override
    public List getList() {
        ArrayList<ExerciseRecordModel> elements = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DATE));
                int time = cursor.getInt(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_TIME));
                float distance = cursor.getFloat(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DISTANCE));
                String comment = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_COMMENT));
                String activity = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ACTIVITY));

                ExerciseRecordModel element = new ExerciseRecordModel();
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
        ExerciseRecordModel element = new ExerciseRecordModel();
        String query = String.format("SELECT * FROM %s WHERE %s=?", table, ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DATE));
            int time = cursor.getInt(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_TIME));
            float distance = cursor.getFloat(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DISTANCE));
            String comment = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_COMMENT));
            String activity = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ACTIVITY));

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
        String whereClause = ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ID + "="
                + ((ExerciseRecordModel) element).getId();
        ContentValues cv = getContentValues(element);
        return database.update(table, cv, whereClause, null);
    }

    @Override
    protected ContentValues getContentValues(Object element) {
        ContentValues cv = new ContentValues();
        cv.put(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DATE, formatForDate.format(((ExerciseRecordModel) element).getDate()));
        cv.put(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_TIME, ((ExerciseRecordModel) element).getTime());
        cv.put(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_DISTANCE, ((ExerciseRecordModel) element).getDistance());
        cv.put(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_COMMENT, ((ExerciseRecordModel) element).getComment());
        cv.put(ActivitiesDatabaseHelper.Tables.ExerciseRecordTable.COLUMN_ACTIVITY, ((ExerciseRecordModel) element).getActivity());

        return cv;
    }
}
