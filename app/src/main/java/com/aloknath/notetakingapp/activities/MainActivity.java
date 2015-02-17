package com.aloknath.notetakingapp.activities;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.broadcast_receiver.MyReceiver;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends ListActivity {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    public static final int MENU_DELETE_ID = 1002;
    private static final int CALENDER_ACTIVITY_REQUEST = 1003;
    private int currentNoteId;
   // private NotesDataSource dataSource;
    private String dayId;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private PendingIntent pendingIntent;
    private Context context;

    private DateDataSource todayDataSource;
    List<NoteItem> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // Adding a Navigation Drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        // For Context Menu
        registerForContextMenu(getListView());
        //getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setTitle("Today's Tasks");

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key = formatter.format(new Date());

        key = key.replace("-","");

        key = "TableNo" + key;
        dayId = key;
        refreshDisplay();

        //Notify
        notification(notesList);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent;
                switch(position){
                    case 0:  intent = new Intent(MainActivity.this, CalenderActivity.class);
                             startActivity(intent);
                             break;
                    case 1:  intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                             startActivity(intent);
                             break;
                    default: break;

                }
            }
        });

    }

    private void notification(List<NoteItem> notes) {

        int hour = 0;
        int min = 0;
        NoteItem noteItem =  NoteItem.getNew();

        for (NoteItem note : notes){
            String noteTime = note.getTime();
            if(noteTime.isEmpty()){
                hour = 0;
                min = 0;
            }else {
                hour = Integer.parseInt(noteTime.substring(0, 2));
                min = Integer.parseInt(noteTime.substring(3, 5));
            }
            noteItem = note;
//            Toast.makeText(this,  String.valueOf(hour), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, String.valueOf(min), Toast.LENGTH_SHORT).show();
            break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min-1);

//        calendar.set(Calendar.MONTH, 2);
//        calendar.set(Calendar.YEAR, 2015);
//        calendar.set(Calendar.DAY_OF_MONTH, 16);

        Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this.context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);



        //calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM,Calendar.PM);

//        Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
//
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 30, pendingIntent);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//
//        //Notification Start
//        if (notes.size() != 0) {
//            Intent myIntent = new Intent(this, NotifyService.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("title", noteItem.getTitle());
//            bundle.putString("description", noteItem.getDescription());
//            bundle.putString("location", noteItem.getLocation());
//            myIntent.putExtras(bundle);
//            //this.startService(myIntent);
//
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.MINUTE, min);
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 30, pendingIntent);
//        }
//        //Notification End
    }

    private void addDrawerItems() {
        String[] osArray = { "Calender", "Editor" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Today's Tasks");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void refreshDisplay() {
        todayDataSource = new DateDataSource(this);
        todayDataSource.open();
        notesList = todayDataSource.getDayItenary(dayId);
        todayDataSource.close();

        // Sort the List Items based on the time entered
        Collections.sort(notesList, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem item1, NoteItem item2) {

                return item1.getTime().compareTo(item2.getTime());
            }
        });

//        for (NoteItem note : notesList){
//            String noteTime = note.getTime();
//            int hour = Integer.parseInt(noteTime.substring(0,2));
//            int min = Integer.parseInt(noteTime.substring(3,5));
//           // String value = String.valueOf(hour);
//            Toast.makeText(this,  String.valueOf(hour), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, String.valueOf(min), Toast.LENGTH_SHORT).show();
//        }

        ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
        setListAdapter(adapter);
    }

    // Commit
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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


    private void createNote() { NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        String pattern = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String id = formatter.format(new Date());
        id = id.replace(":","");
        //Toast.makeText(DayBreakDownActivity.this, dayId + id, Toast.LENGTH_LONG).show();
        intent.putExtra("key", dayId+id);

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
