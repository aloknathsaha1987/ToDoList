package com.aloknath.notetakingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aloknath.notetakingapp.adapter.DayItemAdapter;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.data.NotesDailyDataSource;
import com.aloknath.notetakingapp.data.NotesDataSource;
import com.aloknath.notetakingapp.database.DateDataSource;

import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DayBreakDownActivity extends ListActivity {

    private NotesDailyDataSource hourlyDataSource;
    private List<NoteItem> notesList = new ArrayList<NoteItem>();
    DateDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String day_Table_ID;

        Intent intent = this.getIntent();
        Map<String, String> dayStore = new HashMap<String, String>();
        dayStore.put(intent.getStringExtra("Day_Table"), "NewTable");
        day_Table_ID = dayStore.get(intent.getStringExtra("Day_Table"));
        dataSource = new DateDataSource(this, day_Table_ID);
        dataSource.open();

        hourlyDataSource = new NotesDailyDataSource(this);

        new Thread(){
            public void run(){
                boolean found;
                found = hourlyDataSource.setTableName(day_Table_ID);
                if(found){
                    android.util.Log.i("found table","Table Found and hence not Created");
                    notesList = dataSource.getDayItenary(day_Table_ID);
                    dataSource.close();
                    refreshDisplay();

                }else{
                    android.util.Log.i( "Not found","Table Not Found and hence Created");
                    NoteItem note = new NoteItem();
                    note.setKey("One");
                    note.setText("Time");
                    note.setDescription("Jersey City Meeting");
                    note.setLocation("65 Saint Pauls Avenue");
                    dataSource.addDayItems(note);
                    notesList = dataSource.getDayItenary(day_Table_ID);
                    dataSource.close();
                    refreshDisplay();
                    //Create a new table in the database
                }
            }
        }.start();
//
//        for(int i =1; i <= 24; i++){
//           NoteItem note = NoteItem.getNew(i);
//          // notesList.add(note);
//           hourlyDataSource.updateList(note);
//           android.util.Log.i("The Key Returned", note.getKey());
//        }
//
//        refreshDisplay();
    }

    private void refreshDisplay() {

       // notesList = hourlyDataSource.findAll();
        DayItemAdapter adapter = new DayItemAdapter(this, R.layout.list_item_layout, notesList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        NoteItem note = notesList.get(position);
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("text",note.getText());
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == MainActivity.EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            NoteItem note = new NoteItem();
            note.setKey(data.getStringExtra("key"));
            note.setText(data.getStringExtra("text"));
            //hourlyDataSource.updateList(note);

            // Update the database
            refreshDisplay();
        }
    }
}


