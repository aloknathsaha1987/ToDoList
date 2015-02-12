package com.aloknath.notetakingapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ALOKNATH on 2/9/2015.
 */
public class NoteItem {

    private String key;
    private String time;
    private String description;
    private String location;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static NoteItem getNew(){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);

        String pattern = "yyyy-MM-dd HH:mm:ss Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key = formatter.format(new Date());

        NoteItem note = new NoteItem();
        note.setKey(key);
        note.setTime("Enter Time");
        note.setTitle("Enter Task");
        note.setDescription("Enter Task Description");
        note.setLocation("Enter Task Location");

        return note;
    }

    public static NoteItem getNew(int i){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key = formatter.format(new Date());

        NoteItem note = new NoteItem();
        note.setKey(String.valueOf(i));
        note.setTime("Hour" + i);

        return note;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
