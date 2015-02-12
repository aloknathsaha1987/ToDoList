package com.aloknath.notetakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

/**
 * Created by ALOKNATH on 2/9/2015.
 */
public class NoteEditorActivity extends Activity {

    private NoteItem note;
    private DateDataSource dataSource;
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

        editText = (EditText)findViewById(R.id.titleTime);
        editText.setText(note.getTime());

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
