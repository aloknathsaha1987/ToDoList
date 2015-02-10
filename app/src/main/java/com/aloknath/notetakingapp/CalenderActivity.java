package com.aloknath.notetakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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
                Toast.makeText(CalenderActivity.this, key, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CalenderActivity.this, MainActivity.class);
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
            finish();
        }
        return false;
    }
}
