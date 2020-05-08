package com.example.sporttracker.Services.StepsDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sporttracker.Models.StepsRecordModel;
import com.example.sporttracker.Services.AbstractRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StepsRecordsRepository extends AbstractRepository {

    public StepsRecordsRepository(Context context) {
        super(context);
        dbHelper = new StepsDatabaseHelper(context);
        table = StepsDatabaseHelper.Tables.StepsRecordTable.NAME;
        columns = new String[]{StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_ID,
                StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_DATE,
                StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_HOUR_IN_DAY,
                StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_COUNT};
    }

    @Override
    public List getList() {
        ArrayList<StepsRecordModel> elements = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_DATE));
                int hourInDay = cursor.getInt(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_HOUR_IN_DAY));
                int count = cursor.getInt(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_COUNT));

                StepsRecordModel element = new StepsRecordModel();
                element.setId(id);
                try {
                    element.setDate(formatForDate.parse(date));
                } catch (ParseException e) {
                }
                element.setHourInDay(hourInDay);
                element.setCount(count);

                elements.add(element);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return elements;
    }

    @Override
    public Object getElement(int id) {
        StepsRecordModel element = new StepsRecordModel();
        String query = String.format("SELECT * FROM %s WHERE %s=?", table, StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_DATE));
            int hourInDay = cursor.getInt(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_HOUR_IN_DAY));
            int count = cursor.getInt(cursor.getColumnIndex(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_COUNT));

            element.setId(id);
            try {
                element.setDate(formatForDate.parse(date));
            } catch (ParseException e) {
            }
            element.setHourInDay(hourInDay);
            element.setCount(count);
        }
        cursor.close();
        return element;
    }

    @Override
    public int update(Object element) {
        String whereClause = StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_ID + "="
                + ((StepsRecordModel) element).getId();
        ContentValues cv = getContentValues(element);
        return database.update(table, cv, whereClause, null);
    }

    @Override
    protected ContentValues getContentValues(Object element) {
        ContentValues cv = new ContentValues();
        cv.put(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_DATE, formatForDate.format(((StepsRecordModel) element).getDate()));
        cv.put(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_HOUR_IN_DAY, ((StepsRecordModel) element).getHourInDay());
        cv.put(StepsDatabaseHelper.Tables.StepsRecordTable.COLUMN_COUNT, ((StepsRecordModel) element).getCount());

        return cv;
    }
}
