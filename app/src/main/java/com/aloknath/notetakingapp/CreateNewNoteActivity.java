package com.aloknath.notetakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;
import java.util.List;

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
        item.setText(intent.getStringExtra("text"));
        EditText editText = (EditText)findViewById(R.id.noteText);
        editText.setText(item.getText());
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

        EditText editText = (EditText)findViewById(R.id.noteText);
        String noteText = editText.getText().toString();
        item.setText(noteText);
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
