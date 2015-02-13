package com.aloknath.notetakingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aloknath.notetakingapp.adapter.DayItemAdapter;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.data.NotesDailyDataSource;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DayBreakDownActivity extends ListActivity {

    private NotesDailyDataSource hourlyDataSource;
    private List<NoteItem> notesList = new ArrayList<NoteItem>();
    private DateDataSource dataSource;
    private int currentNoteId;
    private String dayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerForContextMenu(getListView());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final String day_Table_ID;
        final boolean found;

        Intent intent = this.getIntent();
        day_Table_ID = "TableNo"+ intent.getStringExtra("Day_Table");
        dayId = day_Table_ID;

        hourlyDataSource = new NotesDailyDataSource(this);
        found = hourlyDataSource.setTableName("TableNo"+ intent.getStringExtra("Day_Table"));


        new Thread(){
            public void run(){

                if(found){

                    refreshDisplay();

                }else{

                    NoteItem note = new NoteItem();
                    note.setKey(day_Table_ID);
                    note.setTitle("Enter Title");
                    note.setTime("Enter Time");
                    note.setDescription("Enter Description");
                    note.setLocation("Enter Location");

                    dataSource = new DateDataSource(DayBreakDownActivity.this);
                    dataSource.open();
                    dataSource.addDayItems(note);
                    //notesList = dataSource.getDayItenary(day_Table_ID);
                    dataSource.close();
                    refreshDisplay();
                }
            }
        }.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_create:
                createNote();
                break;
            case R.id.action_calender:
                Intent intent = new Intent(this, CalenderActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int) info.id;
        menu.add(0, MainActivity.MENU_DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == MainActivity.MENU_DELETE_ID){
            NoteItem note = notesList.get(currentNoteId);
            dataSource.removeFromList(note);
            refreshDisplay();
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        String pattern = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String id = formatter.format(new Date());
        id = id.replace(":","");
        //Toast.makeText(DayBreakDownActivity.this, dayId + id, Toast.LENGTH_LONG).show();
        intent.putExtra("key", dayId+id);
        intent.putExtra("time",note.getTime());
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());
        intent.putExtra("location",note.getLocation());
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            refreshDisplay();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataSource = new DateDataSource(DayBreakDownActivity.this);
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

        dataSource = new DateDataSource(this);
        dataSource.open();
        notesList = dataSource.getDayItenary(dayId);
        dataSource.close();

       // notesList = hourlyDataSource.findAll();
        DayItemAdapter adapter = new DayItemAdapter(this, R.layout.list_item_layout, notesList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        NoteItem note = notesList.get(position);
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("time",note.getTime());
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());
        intent.putExtra("location",note.getLocation());
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

}


