package com.aloknath.notetakingapp.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ALOKNATH on 2/11/2015.
 */
public class CreateNewNoteActivity extends NoteEditorActivity {

    private NoteItem item;
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
        item = new NoteItem();
        item.setKey(intent.getStringExtra("key"));
        setTime = (EditText)findViewById(R.id.titleTime);

        Button save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDbAndFinish();
            }
        });

        Button cancel = (Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setCurrentDateOnView();

        }
    };

    private void setCurrentDateOnView() {
        String timeFormat = "kk:mm";
        SimpleDateFormat stf = new SimpleDateFormat(timeFormat, Locale.US);
       // setTime.setText(stf.format(calendar.getTime()));
        setTime.setText(stf.format(calendar.getTime()));

    }

    public void timeOnClick(View view){
        new TimePickerDialog(CreateNewNoteActivity.this, time, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show();
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
            saveToDbAndFinish();
        }
        return false;
    }

    private void saveToDbAndFinish() {

        String noteText = setTime.getText().toString();
        item.setTime(noteText);

        EditText editText = (EditText)findViewById(R.id.titleText);
        noteText = editText.getText().toString();
        item.setTitle(noteText);

        editText = (EditText)findViewById(R.id.titleDescription);
        noteText = editText.getText().toString();
        item.setDescription(noteText);

        editText = (EditText)findViewById(R.id.titleLocation);
        noteText = editText.getText().toString();
        item.setLocation(noteText);

        dataSource.addDayItems(item);
        dataSource.close();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        saveToDbAndFinish();
    }

}
