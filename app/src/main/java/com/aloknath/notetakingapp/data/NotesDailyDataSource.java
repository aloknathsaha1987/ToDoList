package com.aloknath.notetakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class NotesDailyDataSource {

    private static final String DAY_PREFKEY = "daily_notes" ;
    private SharedPreferences noteDayPrefs;


    public NotesDailyDataSource(Context context){
        noteDayPrefs = context.getSharedPreferences(DAY_PREFKEY, context.MODE_PRIVATE);

    }

    public boolean setTableName(String day_table_id) {
        boolean found = false;
        Map<String,?> notesMap = noteDayPrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());
        if (keys.size() == 0){
            SharedPreferences.Editor editor = noteDayPrefs.edit();
            editor.putString(day_table_id, "Table_Created");
            editor.commit();

        }else
        {
            for (String key: keys){
                if(key == day_table_id){
                    found = true;
                }
            }
            if (!found){
                SharedPreferences.Editor editor = noteDayPrefs.edit();
                editor.putString(day_table_id, "Table_Created");
                editor.commit();
            }
        }
        return found;
    }
}
