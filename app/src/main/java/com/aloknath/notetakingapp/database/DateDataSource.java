package com.aloknath.notetakingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aloknath.notetakingapp.data.NoteItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DateDataSource {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    private String table_name;

    public static final String[] allColumns =
            {DateDbOpenHelper.DATE_ID,
             DateDbOpenHelper.TIME,
             DateDbOpenHelper.DESCRIPTION,
             DateDbOpenHelper.LOCATION};

    public DateDataSource(Context context
            , String name){
        this.table_name = name;
        sqLiteOpenHelper = new DateDbOpenHelper(context, name);

    }

    public void open(){
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteOpenHelper.close();
    }

    public boolean addDayItems(NoteItem note){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DateDbOpenHelper.DATE_ID, note.getKey());
        contentValues.put(DateDbOpenHelper.TIME, note.getText());
        contentValues.put(DateDbOpenHelper.DESCRIPTION, note.getDescription());
        contentValues.put(DateDbOpenHelper.LOCATION, note.getLocation());

        long insertId = sqLiteDatabase.insert(table_name, null, contentValues);
        return (insertId != -1);
    }

    public List<NoteItem> getDayItenary(String particularDayTable){
        Cursor cursor = sqLiteDatabase.query(particularDayTable, allColumns,
                null, null, null, null,null);
        List<NoteItem> noteItems = cursorToList(cursor);
        return noteItems;
    }

    private List<NoteItem> cursorToList(Cursor cursor) {

        List<NoteItem> noteItems = new ArrayList<NoteItem>();
        if (cursor.getCount()> 0){
            while (cursor.moveToNext()){
                NoteItem noteItem = new NoteItem();
                noteItem.setKey(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.DATE_ID)));
                noteItem.setText(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.TIME)));
                noteItem.setDescription(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.DESCRIPTION)));
                noteItem.setLocation(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.LOCATION)));
                noteItems.add(noteItem);
            }
        }

        return  noteItems;
    }


}
