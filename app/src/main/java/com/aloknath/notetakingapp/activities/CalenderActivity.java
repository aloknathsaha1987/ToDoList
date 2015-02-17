package com.aloknath.notetakingapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.aloknath.notetakingapp.R;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ALOKNATH on 2/9/2015.
 */



public class CalenderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);
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
                startActivity(intent);

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return false;
    }

}


//public class CalenderActivity extends Activity{
//    CalendarView calendar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        //sets the main layout of the activity
//
//        setContentView(R.layout.calender_view);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //initializes the calendarview
//
//        initializeCalendar();
//
//    }
//
//    public void initializeCalendar() {
//
//        calendar = (CalendarView) findViewById(R.id.calendar);
//
//        // sets whether to show the week number.
//
//        calendar.setShowWeekNumber(false);
//
//        // sets the first day of week according to Calendar.
//
//        // here we set Monday as the first day of the Calendar
//
//        calendar.setFirstDayOfWeek(2);
//
//        //The background color for the selected week.
//
//        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
//
//        //sets the color for the dates of an unfocused month.
//        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
//
//        //sets the color for the separator line between weeks.
//
//        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
//
//        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
//
//        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
//
//        //sets the listener to be notified upon selected date change.
//
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//
//                    //show the selected date as a toast
//
//            @Override
//
//            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
//
////                String pattern = "MM-dd-yyyy";
////                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
////                String key = formatter.format(date);
////                key = key.replace("-","");
//                String key = String.valueOf(month) + String.valueOf(day) + String.valueOf(year);
//                //Toast.makeText(CalenderActivity.this, key, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(CalenderActivity.this, DayBreakDownActivity.class);
//                intent.putExtra("Day_Table", key);
//                startActivity(intent);
//
//            }
//
//        });
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home){
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        return false;
//    }
//
//}
