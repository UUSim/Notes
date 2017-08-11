package com.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Simon on 09-08-17.
 */

/**
 * From: https://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public NoteAdapter(Context context, int textViewResourceId, ArrayList<Note> items) {
        super(context, textViewResourceId, items);
        //System.out.println("Creating NoteAdapter");
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            //System.out.println("Inflating the view");
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(android.R.id.text1); // R.id.fragment1

            convertView.setTag(viewHolder);
        } else {
            //System.out.println("Reusing existing view");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note item = getItem(position);
        if (item!= null) {
            //System.out.println("Setting the view text");
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(String.format("%s: %s", item.getModifiedTimeStamp(this.getContext()), item.getText()));
        }

        return convertView;
    }
}