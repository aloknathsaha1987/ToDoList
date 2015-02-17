package com.aloknath.notetakingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aloknath.notetakingapp.data.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DateDataSource {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    public static final String[] allColumns =
            {DateDbOpenHelper.DATE_ID,
             DateDbOpenHelper.TITLE,
             DateDbOpenHelper.TIME,
             DateDbOpenHelper.DESCRIPTION,
             DateDbOpenHelper.LOCATION};

    public DateDataSource(Context context){
        sqLiteOpenHelper = new DateDbOpenHelper(context);

    }

    public void open(){
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteOpenHelper.close();
    }

    public boolean addDayItems(NoteItem note){

        //keytool -list -v -keystore "C:\Users\ALOKNATH\.android\note_app.keystore" -alias Alok -storepass android -keypass android
        //  33:51:D6:2E:AA:B1:BF:BE:94:BA:B9:21:E2:16:6B:19:19:C6:4B:43
        //keytool -list -v -keystore "C:\Users\ALOKNATH\.android\note_app.keystore" -alias Alok
        //keytool -exportcert -alias androiddebugkey -keystore "C:\Users\ALOKNATH\.android\note_app.keystore" | "C:\Users\OpenSSL\bin\openssl" sha1 -binary | "C:\Users\OpenSSL\bin\openssl" base64
        //keytool -exportcert -alias AlokNathSaha -keystore "C:\Users\ALOKNATH\.android\debug_final.keystore"  | "C:\Users\OpenSSL\bin\openssl" sha1 -binary | "C:\Users\OpenSSL\bin\openssl" base64

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DateDbOpenHelper.DATE_ID, note.getKey());
        contentValues.put(DateDbOpenHelper.TITLE, note.getTitle());
        contentValues.put(DateDbOpenHelper.TIME, note.getTime());
        contentValues.put(DateDbOpenHelper.DESCRIPTION, note.getDescription());
        contentValues.put(DateDbOpenHelper.LOCATION, note.getLocation());

        long insertId = sqLiteDatabase.insert(DateDbOpenHelper.TABLENAME, null, contentValues);
        return (insertId != -1);
    }

    /*
            public int updateDayItems(NoteItem note, NoteItem unModifiedNote){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //note.setTitle("Test123");
//        contentValues.put(DateDbOpenHelper.DATE_ID, note.getKey());
        contentValues.put(DateDbOpenHelper.TIME, note.getTime());
        contentValues.put(DateDbOpenHelper.TITLE, note.getTitle());
        contentValues.put(DateDbOpenHelper.DESCRIPTION, note.getDescription());
        contentValues.put(DateDbOpenHelper.LOCATION, note.getLocation());

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DateDbOpenHelper.TABLENAME +
                " WHERE date_id like '%" + unModifiedNote.getKey() + "%' AND task_title like '%" +
                unModifiedNote.getTitle() + "%'", null);

        int id = 0;

        if (cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            Log.i("The _d", String.valueOf(id));
        }


//        String[] args = { note.getKey() };
//        String query =
//                "UPDATE " + DateDbOpenHelper.TABLENAME
//                        + " SET "   + DateDbOpenHelper.TIME + " = " + timeText
//                        + " WHERE " + DateDbOpenHelper.DATE_ID +"=?";
//
//        Cursor cu = sqLiteDatabase.rawQuery( query, args);
//        cu.moveToFirst();
//        cu.close();
//      this.db.update(ENTRY_TABLE, args, ("message = ? AND username = "+user), new String[] {og});

//        return sqLiteDatabase.update(DateDbOpenHelper.TABLENAME, contentValues,
//                DateDbOpenHelper.DATE_ID + " = ? AND " + DateDbOpenHelper.TITLE + " = ? ",
//                new String[] { note.getKey(), note.getTitle() });

        return sqLiteDatabase.update(DateDbOpenHelper.TABLENAME, contentValues,
                "_id" + " = ? ",
                new String[] {String.valueOf(id)});

    }

     */
    public int updateDayItems(NoteItem note){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        contentValues.put(DateDbOpenHelper.DATE_ID, note.getKey());
        contentValues.put(DateDbOpenHelper.TIME, note.getTime());
        contentValues.put(DateDbOpenHelper.TITLE, note.getTitle());
        contentValues.put(DateDbOpenHelper.DESCRIPTION, note.getDescription());
        contentValues.put(DateDbOpenHelper.LOCATION, note.getLocation());

//        String[] args = { note.getKey() };
//        String query =
//                "UPDATE " + DateDbOpenHelper.TABLENAME
//                        + " SET "   + DateDbOpenHelper.TIME + " = " + timeText
//                        + " WHERE " + DateDbOpenHelper.DATE_ID +"=?";
//
//        Cursor cu = sqLiteDatabase.rawQuery( query, args);
//        cu.moveToFirst();
//        cu.close();
//      this.db.update(ENTRY_TABLE, args, ("message = ? AND username = "+user), new String[] {og});

        return sqLiteDatabase.update(DateDbOpenHelper.TABLENAME, contentValues,
                DateDbOpenHelper.DATE_ID + " = ? ",
                new String[] { note.getKey() });

//        return sqLiteDatabase.update(DateDbOpenHelper.TABLENAME, contentValues,
//                DateDbOpenHelper.DATE_ID + " = ? AND " + DateDbOpenHelper.TIME + " = ? ",
//                new String[] { note.getKey(), note.getTime() });

    }

    public List<NoteItem> getDayItenary(String particularDay){

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DateDbOpenHelper.TABLENAME +
                        " WHERE date_id like '%" + particularDay + "%'", null);
//
//        Cursor cursor = sqLiteDatabase.query(particularDayTable, allColumns,
//                null, null, null, null,null);
        List<NoteItem> noteItems = cursorToList(cursor);
        return noteItems;
    }

    private List<NoteItem> cursorToList(Cursor cursor) {

        List<NoteItem> noteItems = new ArrayList<NoteItem>();
        if (cursor.getCount()> 0){
            while (cursor.moveToNext()){
                NoteItem noteItem = new NoteItem();
                noteItem.setKey(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.DATE_ID)));
                noteItem.setTitle(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.TITLE)));
                noteItem.setTime(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.TIME)));
                noteItem.setDescription(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.DESCRIPTION)));
                noteItem.setLocation(cursor.getString(cursor.getColumnIndex(DateDbOpenHelper.LOCATION)));
                noteItems.add(noteItem);
            }
        }

        return  noteItems;
    }


    public int removeFromList(NoteItem note) {

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        return sqLiteDatabase.delete(DateDbOpenHelper.TABLENAME,
                DateDbOpenHelper.DATE_ID + " = ? AND " + DateDbOpenHelper.TITLE + " = ? ",
                new String[] { note.getKey(), note.getTitle() });
    }
}
