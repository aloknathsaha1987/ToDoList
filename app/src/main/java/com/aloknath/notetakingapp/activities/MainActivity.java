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
import android.widget.Toast;

import com.aloknath.notetakingapp.GoogleLicense.GPSLicenseActivity;
import com.aloknath.notetakingapp.GoogleMaps.GetTaskLocationMap;
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
    public static String TODAYKEY;

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
        TODAYKEY = key;
       // Toast.makeText(this, TODAYKEY, Toast.LENGTH_SHORT).show();

        notification(notesList);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent;
                switch(position){
                    case 0:  intent = new Intent(MainActivity.this, CalenderActivity.class);
                             startActivity(intent);
                             break;
                    case 1:  intent = new Intent(MainActivity.this, ScreenSlideActivity.class);
                             startActivity(intent);
                             break;
                    default: break;

                }
            }
        });

    }

    private void notification(List<NoteItem> notes) {

        int hour;
        int min;

        NoteItem noteItem;

        for (NoteItem note : notes){
            String noteTime = note.getTime();
            if(note == null || noteTime.isEmpty()){
//                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
              // Do Nothing
            }else {
                hour = Integer.parseInt(noteTime.substring(0, 2));
                min = Integer.parseInt(noteTime.substring(3, 5));
                noteItem = note;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                int id = hour * 60 + min;

                Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);

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


    private void addDrawerItems() {
        String[] osArray = { "Calender", "Week's Schedule" };
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

                if(item1.getTime().isEmpty() || item2.getTime().isEmpty()){
                    return 0;
                }else {
                    return item1.getTime().compareTo(item2.getTime());
                }

            }
        });


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
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_create:
                createNote();
                break;
            case R.id.googlePlayLicense:
                intent = new Intent(this, GPSLicenseActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createNote() {

        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        String pattern = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String id = formatter.format(new Date());
        id = id.replace(":","");
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
            //Notify
            notification(notesList);
        }else if(requestCode == CALENDER_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            refreshDisplay();
            //Notify
            notification(notesList);
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
