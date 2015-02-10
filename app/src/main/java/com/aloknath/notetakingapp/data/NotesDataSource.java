package com.aloknath.notetakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by ALOKNATH on 2/9/2015.
 */
public class NotesDataSource {

    private static final String PREFKEY = "notes";
    private static final String DAY_PREFKEY = "daily_notes" ;
    private SharedPreferences notePrefs;
    private SharedPreferences noteDayPrefs;

    public NotesDataSource(Context context){
        notePrefs = context.getSharedPreferences(PREFKEY, context.MODE_PRIVATE);
        noteDayPrefs = context.getSharedPreferences(DAY_PREFKEY, context.MODE_MULTI_PROCESS);

    }
    public List<NoteItem> findAll(){

        Map<String,?> notesMap = notePrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());
        List<NoteItem> noteList = new ArrayList<NoteItem>();

        for(String key:keys){
            NoteItem note = new NoteItem();
            note.setKey(key);
            note.setText((String) notesMap.get(key));
            noteList.add(note);
        }
        return  noteList;
    }

    public boolean updateList(List<NoteItem> notes){

        SharedPreferences.Editor editor = noteDayPrefs.edit();

        for(NoteItem note:notes){
            editor.putString(note.getKey(), note.getText());
        }

        editor.commit();

        Map<String, ?> notesMap = noteDayPrefs.getAll();
        for(String key: notesMap.keySet()){
            Log.i("Item Put123", key);
        }

        return true;
    }

    public boolean updateList(NoteItem note, int i){

        SharedPreferences.Editor editor = noteDayPrefs.edit();
        editor.putString(note.getKey(), note.getText());
        editor.commit();
        return true;
    }

    public List<NoteItem> findAll(int day){

        Map<String,?> notesMap = noteDayPrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());
        List<NoteItem> noteList = new ArrayList<NoteItem>();

        for(String key:keys){
            NoteItem note = new NoteItem();
            note.setKey(key);
            note.setText((String) notesMap.get(key));
            noteList.add(note);
            Log.i("Item Putty", key);
        }

        return  noteList;
    }

    public boolean updateList(NoteItem note){

        SharedPreferences.Editor editor = notePrefs.edit();
        editor.putString(note.getKey(), note.getText());
        editor.commit();
        return  true;
    }

    public boolean removeFromList(NoteItem note){
        if(notePrefs.contains(note.getKey())){
            SharedPreferences.Editor editor = notePrefs.edit();
            editor.remove(note.getKey());
            editor.commit();
        }

        return true;
    }
}
