package com.aloknath.notetakingapp.data_preferences;

import android.content.Context;
import android.content.SharedPreferences;

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
   ;
    private SharedPreferences notePrefs;


    public NotesDataSource(Context context){
        notePrefs = context.getSharedPreferences(PREFKEY, context.MODE_PRIVATE);


    }
    public List<NoteItem> findAll(){

        Map<String,?> notesMap = notePrefs.getAll();
        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());
        List<NoteItem> noteList = new ArrayList<NoteItem>();

        for(String key:keys){
            NoteItem note = new NoteItem();
            note.setKey(key);
            note.setTime((String) notesMap.get(key));
            noteList.add(note);
        }
        return  noteList;
    }


    public boolean updateList(NoteItem note){

        SharedPreferences.Editor editor = notePrefs.edit();
        editor.putString(note.getKey(), note.getTime());
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
