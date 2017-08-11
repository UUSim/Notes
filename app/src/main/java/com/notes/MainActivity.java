package com.notes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // Data stores
    protected ArrayList<Note> mNotes = new ArrayList<>();

    public ArrayList<Note> getNotes() {
        return mNotes;
    }
    // *******************************
    // Utility functions
    // *******************************

    protected void showPopup(int resourceStringId) {
        int viewId = R.id.container;
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
        /* Exit the application if quit was pressed */
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }

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


        setContentView(R.layout.activity_main);


        // Load main_menu
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //From: https://stackoverflow.com/questions/35648913/how-to-set-menu-to-toolbar-in-android
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent openSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                openSettingsIntent.putExtra( SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
                openSettingsIntent.putExtra( SettingsActivity.EXTRA_NO_HEADERS, true );
                startActivity(openSettingsIntent);
                break;

            case R.id.action_archive:
                // User chose the "Archive" action, archive the current note
                this.showPopup(R.string.popupArchived);
                break;

            case R.id.action_quit:
                this.quit();
                break;

            case R.id.action_about:
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Exits the application
     */
    private void quit() {
        this.saveData();

        // From: https://stackoverflow.com/questions/35081130/how-to-close-my-application-programmatically-in-android
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

        /* The above code finishes all the activities except for FirstActivity.
        Then we need to finish the FirstActivity's Enter the below code in Firstactivity's oncreate */
    }

    // *******************************
    // Persistence functions
    // *******************************

    protected void loadData() {
        // load tasks from preference
        SharedPreferences prefs = this.getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        try {
            mNotes = (ArrayList<Note>) ObjectSerializer.deserialize(prefs.getString(getString(R.string.notesStorageLabel), ObjectSerializer.serialize(new ArrayList<Note>())));

            System.out.println("Overview of loaded notes:");
            for (Note note: mNotes) {
                System.out.println(note.getModifiedTimeStamp(this) + " - " + note.getText());
            }
            System.out.println("End of loaded notes.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void saveData() {
        // From: https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
        // save the notes list to preference
        SharedPreferences prefs = this.getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(getString(R.string.notesStorageLabel), ObjectSerializer.serialize(mNotes));
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
}


