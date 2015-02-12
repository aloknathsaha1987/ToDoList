package com.aloknath.notetakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

/**
 * Created by ALOKNATH on 2/11/2015.
 */
public class CreateNewNoteActivity extends NoteEditorActivity {

    private NoteItem item;
    private DateDataSource dataSource;
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
        item.setTime(intent.getStringExtra("time"));
        item.setTitle(intent.getStringExtra("title"));
        item.setDescription(intent.getStringExtra("description"));
        item.setLocation(intent.getStringExtra("location"));

        EditText editText = (EditText)findViewById(R.id.titleText);
        editText.setText(item.getTitle());

        editText = (EditText)findViewById(R.id.titleDescription);
        editText.setText(item.getDescription());

        editText = (EditText)findViewById(R.id.titleLocation);
        editText.setText(item.getLocation());

        editText = (EditText)findViewById(R.id.titleTime);
        editText.setText(item.getTime());

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
        //Toast.makeText(this, "Weird: " + item.getKey() , Toast.LENGTH_SHORT).show();
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

        EditText editText = (EditText)findViewById(R.id.titleTime);
        String noteText = editText.getText().toString();
        item.setTime(noteText);

        editText = (EditText)findViewById(R.id.titleText);
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
