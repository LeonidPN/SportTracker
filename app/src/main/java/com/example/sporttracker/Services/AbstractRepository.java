package com.example.sporttracker.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.List;

public abstract class AbstractRepository<T> {

    protected SimpleDateFormat formatForDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    protected SQLiteOpenHelper dbHelper;
    protected SQLiteDatabase database;

    protected String table;
    protected String[] columns;

    public AbstractRepository(Context context){
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

    public long insert(T element) {
        return database.insert(table, null, getContentValues(element));
    }

    public int delete(int elementId){
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(elementId)};
        return database.delete(table, whereClause, whereArgs);
    }

    abstract public int update(T element);

    abstract protected ContentValues getContentValues(T element);

}
