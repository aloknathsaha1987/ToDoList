package com.aloknath.notetakingapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.data_preferences.NoteItem;

import java.util.List;

/**
 * Created by ALOKNATH on 2/20/2015.
 */
public class DrawerAdapter extends ArrayAdapter {

    private String[] items;
    private Context context;

    public DrawerAdapter (Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.items = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.drawer_layout, parent, false);
        String value = items[position];
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

        TextView tv = (TextView) view.findViewById(R.id.drawer_item);
        tv.setTextColor(Color.BLUE);
        tv.setText(value);


        return view;
    }
}
