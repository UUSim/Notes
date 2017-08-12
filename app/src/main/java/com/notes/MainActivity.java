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
import android.util.Log;
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
    private static final String TAG = "MainActivity";

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

        int deleteNoteIndex = -1;

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("EXIT", false)) {
                /* Exit the application if quit was pressed */
                finish();
            }

            deleteNoteIndex = getIntent().getExtras().getInt("DeleteNoteIndex", -1);
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
        mNotes = DataStore.loadNotes(this);
        if (deleteNoteIndex>=0 && deleteNoteIndex < mNotes.size()) {
            Log.i(TAG, "Deleted note: " + deleteNoteIndex);
            mNotes.remove(deleteNoteIndex);
            DataStore.saveNotes(this, mNotes);
        }

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

            case R.id.action_new:
                Intent newNoteIntent = new Intent(MainActivity.this, NoteEditActivity.class);
                newNoteIntent.putExtra("OpenNoteIndex", -1);
                startActivity(newNoteIntent);
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
        DataStore.saveNotes(this, mNotes);

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



    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause, saving data");
        DataStore.saveNotes(this, mNotes);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume, loading data");
        mNotes = DataStore.loadNotes(this);
    }
}


