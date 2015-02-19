package com.aloknath.notetakingapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data.NoteItem;
import com.aloknath.notetakingapp.fragments.ScreenSlidePageFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by ALOKNATH on 2/17/2015.
 */
public class ScreenSlideActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 7;
    private static int current_page;
    private String dayId;
    private int month;
    private int day;
    private int year;
    private String key;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setTitle("Today's Tasks");

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                current_page = position;
                if (position == 0){
                    getActionBar().setTitle("Today's Tasks");
                }else{
                    String weekDay;
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, position);
                    weekDay = dayFormat.format(calendar.getTime());
                    getActionBar().setTitle(weekDay);
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;

            case R.id.action_create:
                createNote(current_page);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void getDays(int i) {

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

        month = Integer.parseInt(key.substring(0, 2));
        day = Integer.parseInt(key.substring(3, 5));
        year = Integer.parseInt(key.substring(6, 10));

        if (month < 10) {
            key = "0" + String.valueOf(month) + String.valueOf(day) + String.valueOf(year);
        } else {
            key = String.valueOf(month) + String.valueOf(day) + String.valueOf(year);
        }

        key = "TableNo" + key;
        dayId = key;
    }


    private void createNote(int pageNo) {
        getDays(pageNo);
        Intent intent = new Intent(this, CreateNewNoteActivity.class);
        String pattern = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String id = formatter.format(new Date());
        id = id.replace(":","");
        intent.putExtra("key", dayId+id);
        startActivityForResult(intent, MainActivity.EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.EDITOR_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
            //onResume();
            Toast.makeText(ScreenSlideActivity.this, "List Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
