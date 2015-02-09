package com.aloknath.notetakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.aloknath.notetakingapp.data.NoteItem;

/**
 * Created by ALOKNATH on 2/9/2015.
 */
public class NoteEditorActivity extends Activity {

    private NoteItem note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        note = new NoteItem();
        note.setKey(intent.getStringExtra("key"));
        note.setText(intent.getStringExtra("text"));

        EditText editText = (EditText)findViewById(R.id.noteText);
        editText.setText(note.getText());
        editText.setSelection(note.getText().length());
    }

    private void saveAndFinish(){
        EditText editText = (EditText)findViewById(R.id.noteText);
        String noteText = editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("key", note.getKey());
        intent.putExtra("text",noteText);
        setResult(RESULT_OK, intent);
        finish();
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
