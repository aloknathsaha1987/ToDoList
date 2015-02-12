package com.aloknath.notetakingapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data.NoteItem;

import java.util.List;

/**
 * Created by ALOKNATH on 2/10/2015.
 */
public class DayItemAdapter extends ArrayAdapter<NoteItem> {

    private List<NoteItem> noteItems;
    private Context context;

    public DayItemAdapter(Context context, int resource, List<NoteItem> objects) {
        super(context, resource, objects);
        this.noteItems = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_note_editor, parent, false);
        NoteItem note = noteItems.get(position);
        EditText editText = (EditText)view.findViewById(R.id.titleText);
        editText.setText(note.getTime());
        return super.getView(position, convertView, parent);
    }
}
