package com.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // UI objects
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;



    // Data stores
    protected ArrayList<Note> mNotes = new ArrayList<>();
    protected Note mCurrentNote = null;

    // *******************************
    // Utility functions
    // *******************************

    protected void showPopup(int viewId, int resourceStringId) {
        Snackbar.make(findViewById(viewId), resourceStringId,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    // *******************************
    // Control functions
    // *******************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            // From: https://stackoverflow.com/questions/22332513/wake-and-unlock-android-phone-screen-when-compile-and-run-project
            // These flags cause the device screen to turn on (and bypass screen guard if possible) when launching.
            // This makes it easy for developers to test the app launch without needing to turn on the device
            // each time and without needing to enable the "Stay awake" option.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

            // From: https://stackoverflow.com/questions/2131948/force-screen-on/2134602#2134602
            // This will make sure that the screen stays on while your window is in the foreground,
            // and only while it is in the foreground. It greatly simplifies this common use case,
            // eliminating any juggling you need to do as your app transitions between states.
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        this.loadData();
        this.startNewNote();


        setContentView(R.layout.activity_main);


        // Load main_menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        /*mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.activity_main, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    /**  Called when the user taps the Archive button */
    public void archiveNote(View view) {
        this.mCurrentNote.setText(this.getNote());

        if (this.mCurrentNote.isFilled()) {
            mNotes.add(this.mCurrentNote);

            // Show short popup that note has been archived
            this.showPopup(view.getId(), R.string.popupArchived);
        }
        else {
            // Show short popup that note has been archived
            this.showPopup(view.getId(), R.string.popupEmpty);
        }

        this.startNewNote();
    }

    public void startNewNote() {
        this.mCurrentNote = new Note(new String(), new Date());
        this.setNote(mCurrentNote.getText());
        this.setModified(mCurrentNote.getModifiedTimeStamp());
    }

    /** Called when the user taps the Send button */
    public void sendNote(View view) {
        String note = this.getNote();

        if (note.length()>0) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, note);
            sendIntent.setType("text/plain");

            // Optional: Send via WhatsApp
            //sendIntent.setPackage("com.whatsapp");

            // Go
            startActivity(sendIntent);
        }
        else {
            this.showPopup(view.getId(), R.string.popupEmpty);
        }
    }


    /** Swaps fragments in the main content view */
    /*private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }*/

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        getActionBar().setTitle(title);
    }



    // *******************************
    // UI data functions
    // *******************************

    protected void setModified(String modified) {
        TextView modifiedView = (TextView) findViewById(R.id.modified);
        modifiedView.setText(modified);
    }

    protected void setNote(String text) {
        EditText note = (EditText) findViewById(R.id.note);
        note.setText(text);
    }

    protected String getNote() {
        EditText note = (EditText) findViewById(R.id.note);
        return note.getText().toString();
    }

    // *******************************
    // Persistence functions
    // *******************************

    protected void loadData() {
        /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.note);
        String note = sharedPref.getString(getString(R.string.note), "Default...");
        //this.setModified(timestamp);
        this.setNote(note);*/

        // load tasks from preference
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);

        try {
            mNotes = (ArrayList<Note>) ObjectSerializer.deserialize(prefs.getString(getString(R.string.notes), ObjectSerializer.serialize(new ArrayList<Note>())));

            System.out.println("Overview of loaded notes:");
            for (Note note: mNotes) {
                System.out.println(note.getModifiedTimeStamp() + " - " + note.getText());
            }
            System.out.println("End of loaded notes.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void saveData() {
        // Save the note to preferences
        /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.note), this.getNote());
        editor.apply();*/



        // From: https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
        // save the notes list to preference
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(getString(R.string.notes), ObjectSerializer.serialize(mNotes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.saveData();
    }

    /*private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }*/
}


