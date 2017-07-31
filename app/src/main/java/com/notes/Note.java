package com.notes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
            //this.dirty = true;
        }
    }

    public boolean isFilled() {
        return (this.mText.length()>0);
    }

    /*public boolean isDirty() {
        return this.dirty;
    }

    public void clearDirty() {
        this.dirty = false;
    }*/

    /** Set the current timestamp in the 'modified' field */
    public void updateTimeStamp() {
        this.mModified = new Date();
    }

    public String getModifiedTimeStamp() {
        //"dd-MM-yyyy HH:mm:ss"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(this.mModified);
    }
}
