package com.aloknath.notetakingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends ListActivity {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    public static final int MENU_DELETE_ID = 1002;
    private static final int CALENDER_ACTIVITY_REQUEST = 1003;
    private int currentNoteId;
   // private NotesDataSource dataSource;
    private String dayId;

    private DateDataSource todayDataSource;
    List<NoteItem> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For Context Menu
        registerForContextMenu(getListView());
        //getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setTitle("Today's Tasks");

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key = formatter.format(new Date());

        key = key.replace("-","");

        key = "TableNo" + key;
        dayId = key;
        refreshDisplay();

    }

    private void refreshDisplay() {
        todayDataSource = new DateDataSource(this);
        todayDataSource.open();
        notesList = todayDataSource.getDayItenary(dayId);
        todayDataSource.close();
        //notesList = dataSource.findAll();
        ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
        setListAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        todayDataSource = new DateDataSource(MainActivity.this);
        todayDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        todayDataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todayDataSource.close();
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
        switch (id){
            case R.id.action_create:
                createNote();
                break;
            case R.id.action_calender:
                Intent intent = new Intent(this, CalenderActivity.class);
                startActivityForResult(intent, CALENDER_ACTIVITY_REQUEST);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createNote() {
        NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        intent.putExtra("key", dayId);
        intent.putExtra("time",note.getTime());
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());
        intent.putExtra("location",note.getLocation());
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
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
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            refreshDisplay();
        }else if(requestCode == CALENDER_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            refreshDisplay();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int) info.id;
        menu.add(0, MENU_DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == MENU_DELETE_ID){
            NoteItem note = notesList.get(currentNoteId);
            todayDataSource.removeFromList(note);
            refreshDisplay();
        }
        return super.onContextItemSelected(item);
    }
}
