package com.aloknath.notetakingapp.activities;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aloknath.notetakingapp.GoogleLicense.GPSLicenseActivity;
import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.adapter.DayItemAdapter;
import com.aloknath.notetakingapp.broadcast_receiver.MyReceiver;
import com.aloknath.notetakingapp.data_preferences.NoteItem;
import com.aloknath.notetakingapp.data_preferences.NotesDailyDataSource;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerForContextMenu(getListView());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        final String day_Table_ID;
        final boolean found;

        Intent intent = this.getIntent();
        day_Table_ID = "TableNo"+ intent.getStringExtra("Day_Table");
        dayId = day_Table_ID;
        getActionBar().setTitle(intent.getStringExtra("Day_Selected"));

        hourlyDataSource = new NotesDailyDataSource(this);
        found = hourlyDataSource.setTableName("TableNo"+ intent.getStringExtra("Day_Table"));


        new Thread(){
            public void run(){

                if(found){
                    refreshDisplay();
                    if(dayId == MainActivity.TODAYKEY){
                        notification(notesList);
                    }
                }
            }
        }.start();


    }

    private void notification(List<NoteItem> notes) {

        int hour;
        int min;

        NoteItem noteItem;

        for (NoteItem note : notes){
            String noteTime = note.getTime();
            if( note == null || noteTime.isEmpty()){
                // Do Nothing
            }else {
                hour = Integer.parseInt(noteTime.substring(0, 2));
                if(hour > 0 ){
                    hour = hour -1;
                }else if (hour == 0){
                    hour = 23;
                }
                min = Integer.parseInt(noteTime.substring(3, 5));
                noteItem = note;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                int id = hour * 60 + min;

                Intent myIntent = new Intent(DayBreakDownActivity.this, MyReceiver.class);

                if(noteItem != null){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", noteItem.getTitle());
                    bundle.putString("description", noteItem.getDescription());
                    bundle.putString("location", noteItem.getLocation());
                    bundle.putString("time", noteItem.getTime());
                    bundle.putString("key", noteItem.getKey());
                    bundle.putInt("notificationId", id);

                    myIntent.putExtras(bundle);
                }

                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getBroadcast(this.context, id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 30, pendingIntent);

            }

        }

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
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_create:
                createNote();
                break;
            case R.id.googlePlayLicense:
                 intent = new Intent(this, GPSLicenseActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                if(notesList.size() != 0){
                    //Mark the calender
                    intent = new Intent();
                    intent.putExtra("Content Present", true);
                    setResult(RESULT_OK, intent);
                }else{
                    intent = new Intent();
                    intent.putExtra("Content Present", false);
                    setResult(RESULT_OK, intent);
                }
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
            if(dayId == MainActivity.TODAYKEY){
                notification(notesList);
            }
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

        // Sort the List Items based on the time entered

        Collections.sort(notesList, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem item1, NoteItem item2)
            {
                if(item1.getTime().isEmpty() || item2.getTime().isEmpty()){
                    return 0;
                }else {
                    return item1.getTime().compareTo(item2.getTime());
                }

            }
        });


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


