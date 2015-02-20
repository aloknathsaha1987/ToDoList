package com.aloknath.notetakingapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data_preferences.DatesInWhichTaskStored;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by ALOKNATH on 2/9/2015.
 */

public class CalenderActivity extends Activity {

    private static final int CONTENT_PRESENT = 1005;
    CalendarPickerView calendar;
    DatesInWhichTaskStored datesSharedPrefs;
    private List<Date> datesHighlighted = new ArrayList<Date>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        datesSharedPrefs = new DatesInWhichTaskStored(this);


        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);
        calendar.setBackgroundColor(Color.CYAN);
        refreshHighlights();
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                String pattern = "MM-dd-yyyy";
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                String key = formatter.format(date);
                key = key.replace("-","");
                //Toast.makeText(CalenderActivity.this, key, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CalenderActivity.this, DayBreakDownActivity.class);
                intent.putExtra("Day_Table", key);
                startActivityForResult(intent, CONTENT_PRESENT);

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    private void refreshHighlights() {
        datesHighlighted = datesSharedPrefs.getHighlightedDates();
        if(datesHighlighted.size() != 0) {
            calendar.highlightDates(datesHighlighted);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_PRESENT && resultCode == RESULT_OK) {
            boolean contentValue = data.getBooleanExtra("Content Present", false);
            if(contentValue){
                //Toast.makeText(CalenderActivity.this, "The List is not null: "+ String.valueOf(datesHighlighted.size()), Toast.LENGTH_LONG).show();
                // Search Shared Preferences for the Date
                boolean present = datesSharedPrefs.searchHighlightedDates(String.valueOf(calendar.getSelectedDate()));
                // If return is true, date already present
                // Display Highlighted Table.
                if(present){
                    //Toast.makeText(CalenderActivity.this,String.valueOf(calendar.getSelectedDate()) + " Date Present in shared prefs so not added: " + String.valueOf(datesHighlighted.size()), Toast.LENGTH_LONG).show();
                    calendar.highlightDates(datesHighlighted);
                }
                // If returns false, add the date
                // Update the datesHighlighted Arraylist
                // Display Highlighted Table
                else{

                    datesSharedPrefs.setHighlightDate(String.valueOf(calendar.getSelectedDate()));
                    datesHighlighted = datesSharedPrefs.getHighlightedDates();
                    //Toast.makeText(CalenderActivity.this,"Date Not Present in shared prefs so added: "+ String.valueOf(datesHighlighted.size()), Toast.LENGTH_LONG).show();
                    calendar.highlightDates(datesHighlighted);
                }

            }else{
                //Toast.makeText(CalenderActivity.this, "The List is null: "+ String.valueOf(datesHighlighted.size()), Toast.LENGTH_LONG).show();
                // Remove the marker from the date
                datesSharedPrefs.removeMarker(String.valueOf(calendar.getSelectedDate()));
                refreshHighlights();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
        }
        return false;
    }

}

