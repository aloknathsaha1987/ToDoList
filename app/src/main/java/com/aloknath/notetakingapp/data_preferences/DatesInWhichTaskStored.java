package com.aloknath.notetakingapp.data_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by ALOKNATH on 2/20/2015.
 */
public class DatesInWhichTaskStored {

    private static final String TABLE_PREF_DATES = "table_dates";
    private SharedPreferences taskDaysPrefs;

    public DatesInWhichTaskStored(Context context){
        taskDaysPrefs = context.getSharedPreferences(TABLE_PREF_DATES, context.MODE_PRIVATE);
    }

    public boolean setHighlightDate(String date){

        SharedPreferences.Editor editor = taskDaysPrefs.edit();
        editor.putString(date, "Date Entered: " + date );
        editor.commit();
        return true;
    }

    public boolean searchHighlightedDates(String date){

        Map<String, ?> highlightMap = taskDaysPrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(highlightMap.keySet());

        if(keys.size() == 0){
            return false;
        }
        else{
            for (String key: keys){
                if(key.equals(date))
                return true;
            }
        }
        return false;
}

    public List<Date> getHighlightedDates(){
        List<Date> datesSelectedList = new ArrayList<Date>();
        Map<String, ?> highlightMap = taskDaysPrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(highlightMap.keySet());

        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date startDate = null;

        if(keys.size() != 0) {
            for (String key : keys) {
                try {
                    startDate = df.parse(key);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datesSelectedList.add(startDate);
            }
        }

        return datesSelectedList;
    }

    public void removeMarker(String date){

        if(taskDaysPrefs.contains(date)) {
            Map<String, ?> highlightMap = taskDaysPrefs.getAll();
            SortedSet<String> keys = new TreeSet<String>(highlightMap.keySet());

            if (keys.size() != 0) {
                for (String key : keys) {
                    if (key.equals(date)) {
                        SharedPreferences.Editor editor = taskDaysPrefs.edit();
                        editor.remove(date);
                        editor.commit();
                    }
                }
            }
        }
    }
}
