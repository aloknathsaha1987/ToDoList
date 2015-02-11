package com.aloknath.notetakingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.aloknath.notetakingapp.adapter.DayItemAdapter;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.data.NotesDailyDataSource;
import com.aloknath.notetakingapp.database.DateDataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DayBreakDownActivity extends ListActivity {

    private NotesDailyDataSource hourlyDataSource;
    private List<NoteItem> notesList = new ArrayList<NoteItem>();
    private DateDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String day_Table_ID;
        final boolean found;

        Intent intent = this.getIntent();
        day_Table_ID = "TableNo"+ intent.getStringExtra("Day_Table");

        hourlyDataSource = new NotesDailyDataSource(this);
        found = hourlyDataSource.setTableName("TableNo"+ intent.getStringExtra("Day_Table"));

        dataSource = new DateDataSource(this);
        dataSource.open();

        new Thread(){
            public void run(){

                if(found){
                    notesList = dataSource.getDayItenary(day_Table_ID);
                    dataSource.close();
                    refreshDisplay();

                }else{

                    NoteItem note = new NoteItem();
                    note.setKey(day_Table_ID);
                    note.setText("Testing123");
                    note.setDescription("Jersey City Meeting");
                    note.setLocation("65 Saint Pauls Avenue");
                    dataSource.addDayItems(note);
                    notesList = dataSource.getDayItenary(day_Table_ID);
                    dataSource.close();
                    refreshDisplay();
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


