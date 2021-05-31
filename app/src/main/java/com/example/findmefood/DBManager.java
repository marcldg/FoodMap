package com.example.findmefood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager
{
    private SQLDBHelper sqldbHelper;
    private Context context;
    private SQLiteDatabase database;

    // Class constructor.
    public DBManager(Context contxt)
    {
        context = contxt;
    }

    // Method to open database.
    public DBManager open() throws SQLException
    {
        sqldbHelper = new SQLDBHelper(context);
        database = sqldbHelper.getWritableDatabase();
        return this;
    }

    // Function to close database.
    public void close()
    {
        sqldbHelper.close();
    }

    // Function to insert entry to database.
    public void insert(String restaurant, String location)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLDBHelper.RESTAURANT, restaurant);
        contentValues.put(SQLDBHelper.LOCATION, location);
        database.insert(SQLDBHelper.TABLE_NAME,null, contentValues);
    }

    // Method to fetch items from the database.
    public Cursor fetch()
    {
        String[] columns = new String[] {SQLDBHelper._ID, SQLDBHelper.RESTAURANT, SQLDBHelper.LOCATION};
        Cursor cursor = database.query(SQLDBHelper.TABLE_NAME, columns, null, null, null, null, null);
        return  cursor;
    }

    // Method to update existing entry in database.
    public int update(long _id, String restaurant, String location)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLDBHelper.RESTAURANT, restaurant);
        contentValues.put(SQLDBHelper.LOCATION, location);

        int i = database.update(SQLDBHelper.TABLE_NAME, contentValues, SQLDBHelper._ID + " = " + _id, null);

        return  i;
    }

    // Function to delete existing entry in database.
    public void delete(long _id)
    {
        database.delete(SQLDBHelper.TABLE_NAME, SQLDBHelper._ID + " = " + _id, null);
    }
}
