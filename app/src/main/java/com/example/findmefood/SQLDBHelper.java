package com.example.findmefood;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper
{
    // Creating a table name.
    public static final String TABLE_NAME = "LOCATIONS";

    // Creating the columns from the table.
    public static final String _ID = "_id";
    public static final String RESTAURANT = "name";
    public static final String LOCATION = "location";

    // Creating the Database information
    static final String DB_NAME = "MY_RESTAURANTS.DB";

    // Setting the Database Version.
    static final int DB_VERSION = 1;

    // Creating the Table Query.
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RESTAURANT + " TEXT NOT NULL, " + LOCATION + " TEXT);";

    // Creating the constructor for the Database helper
    public SQLDBHelper(Context context)
    {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executing the Query.
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}