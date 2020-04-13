package com.example.sporttracker.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public abstract class AbstractRepository<T> {

    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase database;

    protected String table;
    protected String[] columns;

    public AbstractRepository(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public AbstractRepository open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    protected Cursor getAllEntries(){
        return  database.query(table, columns, null, null, null, null, null);
    }

    abstract public List getList();

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, table);
    }

    abstract public T getElement(int id);

    abstract public long insert(T element);

    public int delete(int elementId){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(elementId)};
        return database.delete(table, whereClause, whereArgs);
    }

    abstract public int update(T element);

    abstract ContentValues getContentValues(T element);

}
