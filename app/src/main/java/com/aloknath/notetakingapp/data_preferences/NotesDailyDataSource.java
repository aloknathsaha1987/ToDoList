package com.aloknath.notetakingapp.data_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class NotesDailyDataSource {

    private static final String TABLE_PREFKEY = "table_names" ;
    private SharedPreferences noteDayPrefs;

    public NotesDailyDataSource(Context context){
        noteDayPrefs = context.getSharedPreferences(TABLE_PREFKEY, context.MODE_PRIVATE);

    }

    public boolean setTableName(String day_table_id) {

        Map<String,?> notesMap = noteDayPrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());

        if (keys.isEmpty()){
            SharedPreferences.Editor editor = noteDayPrefs.edit();
            editor.putString(day_table_id, "Table_Created");
            editor.commit();
            return false;

        }else
        {
            for (String key: keys){
                if(key.equals(day_table_id)){
                   return true;
                }
            }
            SharedPreferences.Editor editor = noteDayPrefs.edit();
            editor.putString(day_table_id, "Table_Created");
            editor.commit();
            return false;

        }

    }

}
