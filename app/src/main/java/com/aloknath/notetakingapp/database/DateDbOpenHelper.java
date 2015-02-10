package com.aloknath.notetakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DateDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "to_do_test@1.db";
    private static final int DATABASE_VERSION =1;

    public static final String DATE_ID = "date_id";
    public static final String TIME = "time";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";

    private String tableName;
    private String dayTable;

    public DateDbOpenHelper(Context context, String name) {
        super(context, DATABASENAME, null, DATABASE_VERSION);
        this.tableName = name;
        dayTable ="CREATE TABLE IF NOT EXISTS " + tableName + "( " +
                 DATE_ID + " TEXT, " +
                 TIME + " TEXT, " +
                 DESCRIPTION + " TEXT, " +
                 LOCATION + " TEXT " + ")";

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
