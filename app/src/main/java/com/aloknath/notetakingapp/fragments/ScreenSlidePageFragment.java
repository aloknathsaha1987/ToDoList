package com.aloknath.notetakingapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.activities.MainActivity;
import com.aloknath.notetakingapp.activities.NoteEditorActivity;
import com.aloknath.notetakingapp.activities.ScreenSlideActivity;
import com.aloknath.notetakingapp.adapter.DayItemAdapter;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.database.DateDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ALOKNATH on 2/17/2015.
 */
public class ScreenSlidePageFragment extends ListFragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private String dayId;
    private int month;
    private int day;
    private int year;
    private String key;
    private DateDataSource dataSource;
    private List<NoteItem> notesListShare;
    private DayItemAdapter adapter;
    private int currentNoteId;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {

        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public ScreenSlidePageFragment() {

    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        //registerForContextMenu(getListView());
        super.onActivityCreated(savedInstanceState);
        refreshDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
         ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MainActivity.MENU_DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(notesListShare.size() == 0){
            Toast.makeText(getActivity(), "Delete Not Working", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
            if (item.getItemId() == MainActivity.MENU_DELETE_ID) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                currentNoteId = info.position;
                NoteItem note = notesListShare.get(currentNoteId);
                dataSource = new DateDataSource(getActivity());
                dataSource.open();
                dataSource.removeFromList(note);
                dataSource.close();
                refreshDisplay();
            }
        }
        return super.onContextItemSelected(item);
    }

    private void refreshDisplay() {
        getDays(mPageNumber);
        callDisplayAdapter();
    }

    private void callDisplayAdapter() {
        adapter = new DayItemAdapter(getActivity(), R.layout.list_item_layout, notesListShare);
        setListAdapter(adapter);
    }

    private void getDays(int i){

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        key = formatter.format(new Date());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(key));
            calendar.add(Calendar.DATE, i);
            key = formatter.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        month = Integer.parseInt(key.substring(0,2));
        day = Integer.parseInt(key.substring(3,5));
        year = Integer.parseInt(key.substring(6,10));

        if (month <10){
            key = "0" + String.valueOf(month)+ String.valueOf(day) + String.valueOf(year);
        }else{
            key = String.valueOf(month)+ String.valueOf(day) + String.valueOf(year);
        }

        key = "TableNo" + key;
        dayId = key;
        dataSource = new DateDataSource(getActivity());
        dataSource.open();
        notesListShare = dataSource.getDayItenary(dayId);
        dataSource.close();

        // Sort the List Items based on the time entered
        Collections.sort(notesListShare, new Comparator<NoteItem>() {
            @Override
            public int compare(NoteItem item1, NoteItem item2) {
                if(item1.getTime().isEmpty() || item2.getTime().isEmpty()){
                    return 0;
                }else {
                    return item1.getTime().compareTo(item2.getTime());
                }
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        NoteItem note = notesListShare.get(position);
        Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("time",note.getTime());
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description",note.getDescription());
        intent.putExtra("location",note.getLocation());
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.EDITOR_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
            //onResume();
            Toast.makeText(getActivity(), "List Updated", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Returns the page number represented by this fragment object.
     * java.lang.RuntimeException: Content has view with id attribute 'android.R.id.list' that is not a ListView class
     */


    public int getPageNumber() {
        return mPageNumber;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDays(mPageNumber);
        adapter.swapItems(notesListShare);
        callDisplayAdapter();
        dataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}

