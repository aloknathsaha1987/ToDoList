package com.aloknath.notetakingapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data_preferences.NoteItem;

import java.util.List;
import java.util.Random;

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
//        if(convertView!=null){
//            CheckedTextView textView = (CheckedTextView)convertView;
//            textView.setText("the text for item "+position);
//            textView.setTextColor(Color.MAGENTA);
//            textView.setBackgroundColor(Color.CYAN);
//            return textView;
//        }else{
//            CheckedTextView textView = new CheckedTextView(parent.getContext());
//            textView.setText("the text for item "+position);
//            textView.setTextColor(Color.GREEN);
//            textView.setBackgroundColor(Color.CYAN);
//            return textView;
//        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        NoteItem note = noteItems.get(position);
        String[] colors = {"#F08080", "#FFA07A", "#90EE90", "#E0FFFF", "#87CEFA", "#FFFFE0", "#FFB6C1", "#B0C4DE", "#D3D3D3", "#7FFFD4"};

        int modPosition = position % 10;

        switch (modPosition){
            case 0:
                view.setBackgroundColor(Color.parseColor(colors[0]));
                break;
            case 1:
                view.setBackgroundColor(Color.parseColor(colors[1]));
                break;
            case 2:
                view.setBackgroundColor(Color.parseColor(colors[2]));
                break;
            case 3:
                view.setBackgroundColor(Color.parseColor(colors[3]));
                break;
            case 4:
                view.setBackgroundColor(Color.parseColor(colors[4]));
                break;
            case 5:
                view.setBackgroundColor(Color.parseColor(colors[5]));
                break;
            case 6:
                view.setBackgroundColor(Color.parseColor(colors[6]));
                break;
            case 7:
                view.setBackgroundColor(Color.parseColor(colors[7]));
                break;
            case 8:
                view.setBackgroundColor(Color.parseColor(colors[8]));
                break;
            case 9:
                view.setBackgroundColor(Color.parseColor(colors[9]));
                break;
            default:
                break;
        }

        TextView tv = (TextView) view.findViewById(R.id.task_title);
        tv.setTextColor(Color.BLUE);
        tv.setText(note.getTitle());

        tv = (TextView) view.findViewById(R.id.task_time);
        tv.setTextColor(Color.BLUE);
        if(note.getTime() != null) {
            tv.setText(note.getTime());
        }
        return view;
    }

    public void swapItems(List<NoteItem> items) {
        this.noteItems = items;
        notifyDataSetChanged();
    }
}
