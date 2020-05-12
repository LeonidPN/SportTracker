package com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Services.Repositories.Databases.AbstractDatabaseRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class WeightRecordsRepository extends AbstractDatabaseRepository {

    public WeightRecordsRepository(Context context) {
        super(context);
        dbHelper = new ActivitiesDatabaseHelper(context);
        table = ActivitiesDatabaseHelper.Tables.WeightRecordTable.NAME;
        columns = new String[]{ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_ID,
                ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_DATE,
                ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_WEIGHT};
    }

    @Override
    public List getList() {
        ArrayList<WeightRecordModel> elements = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_DATE));
                float weight = cursor.getFloat(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_WEIGHT));

                WeightRecordModel element = new WeightRecordModel();
                element.setId(id);
                try {
                    element.setDate(formatForDate.parse(date));
                } catch (ParseException e) {
                }
                element.setWeight(weight);

                elements.add(element);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return elements;
    }

    @Override
    public Object getElement(int id) {
        WeightRecordModel element = new WeightRecordModel();
        String query = String.format("SELECT * FROM %s WHERE %s=?", table, ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_DATE));
            float weight = cursor.getFloat(cursor.getColumnIndex(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_WEIGHT));

            element.setId(id);
            try {
                element.setDate(formatForDate.parse(date));
            } catch (ParseException e) {
            }
            element.setWeight(weight);
        }
        cursor.close();
        return element;
    }

    @Override
    public int update(Object element) {
        String whereClause = ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_ID + "="
                + ((WeightRecordModel) element).getId();
        ContentValues cv = getContentValues(element);
        return database.update(table, cv, whereClause, null);
    }

    @Override
    protected ContentValues getContentValues(Object element) {
        ContentValues cv = new ContentValues();
        cv.put(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_DATE, formatForDate.format(((WeightRecordModel) element).getDate()));
        cv.put(ActivitiesDatabaseHelper.Tables.WeightRecordTable.COLUMN_WEIGHT, ((WeightRecordModel) element).getWeight());

        return cv;
    }
}
