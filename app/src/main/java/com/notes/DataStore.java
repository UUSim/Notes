package com.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Simon on 11-08-17.
 */

public class DataStore {
    private static final String TAG = "DataStore";

    public static void saveNotes(Context context, ArrayList<Note> notes) {
        // From: https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
        // save the notes list to preference
        SharedPreferences prefs = context.getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(context.getString(R.string.notesStorageLabel), ObjectSerializer.serialize(notes));
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        editor.apply();
    }

    public static ArrayList<Note> loadNotes(Context context) {
        // load tasks from preference
        SharedPreferences prefs = context.getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        ArrayList<Note> notes = new ArrayList<>();
        try {
            notes = (ArrayList<Note>) ObjectSerializer.deserialize(
                    prefs.getString(context.getString(R.string.notesStorageLabel), ObjectSerializer.serialize(new ArrayList<Note>())));

            Log.v(TAG, "Overview of loaded notes:");
            for (Note note: notes) {
                Log.v(TAG, note.getModifiedTimeStamp(context) + " - " + note.getText());
            }
            Log.v(TAG, "End of loaded notes.");
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException", e);
        }
        return notes;
    }
}
