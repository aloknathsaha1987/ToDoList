package com.aloknath.notetakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DateDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "to_do_test@7.db";
    private static final int DATABASE_VERSION =1;

    public static final String DATE_ID = "date_id";
    public static final String TIME = "time";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";

    public static final String TABLENAME = "ToDoTasks_Table";
    private String dayTable ="CREATE TABLE IF NOT EXISTS " + TABLENAME + "( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATE_ID + " TEXT, " +
            TIME + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            LOCATION + " TEXT " + ")";

    public DateDbOpenHelper(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(dayTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        database.execSQL("DROP TABLE IF EXISTS "+ dayTable);
        onCreate(database);
    }
}
