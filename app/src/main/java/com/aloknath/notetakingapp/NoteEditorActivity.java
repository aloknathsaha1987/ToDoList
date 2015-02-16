package com.aloknath.notetakingapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ALOKNATH on 2/9/2015.
 */
public class NoteEditorActivity extends Activity {

    private NoteItem note;
    private DateDataSource dataSource;
    final Calendar calendar = Calendar.getInstance();
    EditText setTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dataSource = new DateDataSource(this);
        dataSource.open();
        Intent intent = this.getIntent();
        note = new NoteItem();
        note.setKey(intent.getStringExtra("key"));
        note.setTime(intent.getStringExtra("time"));
        note.setTitle(intent.getStringExtra("title"));
        note.setDescription(intent.getStringExtra("description"));
        note.setLocation(intent.getStringExtra("location"));

        EditText editText = (EditText)findViewById(R.id.titleText);
        editText.setText(note.getTitle());

        editText = (EditText)findViewById(R.id.titleDescription);
        editText.setText(note.getDescription());

        editText = (EditText)findViewById(R.id.titleLocation);
        editText.setText(note.getLocation());

        setTime = (EditText)findViewById(R.id.titleTime);
        setTime.setText(note.getTime());

        Button save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndFinish();
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //editText.setSelection(note.getTime().length());
    }

    public void saveAndFinish(){

        Intent intent = new Intent();
        intent.putExtra("key", note.getKey());

        EditText editText = (EditText)findViewById(R.id.titleText);
        String noteText = editText.getText().toString();
        intent.putExtra("title",noteText);
        note.setTitle(noteText);

        editText = (EditText)findViewById(R.id.titleTime);
        noteText = editText.getText().toString();
        intent.putExtra("time",noteText);
        note.setTime(noteText);

        editText = (EditText)findViewById(R.id.titleDescription);
        noteText = editText.getText().toString();
        intent.putExtra("description",noteText);
        note.setDescription(noteText);

        editText = (EditText)findViewById(R.id.titleLocation);
        noteText = editText.getText().toString();
        intent.putExtra("location",noteText);
        note.setLocation(noteText);

        setResult(RESULT_OK, intent);

        dataSource.updateDayItems(note);
        dataSource.close();
        finish();
    }

    Time timer;
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setCurrentDateOnView();

        }
    };

    private void setCurrentDateOnView() {
        String timeFormat = "hh:mm a";
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.US);
        setTime.setText(stf.format(calendar.getTime()));

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
        }
    };

    public void timeOnClick(View view){
        new TimePickerDialog(NoteEditorActivity.this, time, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true ).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            saveAndFinish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
}
