package com.notes;

import android.content.Context;
import android.content.res.Resources;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Note implements Serializable{
    private String mText;
    private Date mModified;
    ///protected boolean dirty;

    public Note(String text, Date modified) {
        this.mText = text;
        this.mModified = modified;
        // this.dirty = false;
    }

    public String getText() {
        return this.mText;
    }

    /** Updates the note's text when different */
    public void setText(String newText) {
        if (!this.mText.equals(newText)) {
            this.mText  = newText;
            this.updateTimeStamp();
        }
    }

    public boolean isFilled() {
        return (this.mText.length()>0);
    }


    /** Set the current timestamp in the 'modified' field */
    public void updateTimeStamp() {
        this.mModified = new Date();
    }

    public String getModifiedTimeStamp(Context context) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTime(this.mModified);
        String modifiedFormat = "dd-MM-yyyy";
        String prefix;

        if(now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            prefix = context.getString(R.string.prefix_today);
            modifiedFormat = "HH:mm:ss";
        }
        else {
            now.add(Calendar.DAY_OF_YEAR, -1);
            if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
                prefix = context.getString(R.string.prefix_yesterday);
                modifiedFormat = "HH:mm";
            }
            else {
                prefix = context.getString(R.string.prefix_older);
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(modifiedFormat);
        return prefix + " " + simpleDateFormat.format(this.mModified);
    }
}
