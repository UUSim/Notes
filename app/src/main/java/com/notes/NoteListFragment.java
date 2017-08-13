package com.notes;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "NoteListFragment";
    private NoteAdapter mAdapter;

    public void x() {
        ArrayList<Note> notes = ((MainActivity)this.getActivity()).getNotes();
        mAdapter = new NoteAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                notes);

        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        //x();
        //setEmptyText(getText(R.string.info_no_notes));
        //mAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated()");
        x();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()");
        mAdapter.clear();
        mAdapter.addAll(((MainActivity)this.getActivity()).getNotes());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        this.openNote(position);
    }

    private void openNote(int index) {
        Intent openNoteIntent = new Intent(this.getActivity(), NoteEditActivity.class);
        openNoteIntent.putExtra("OpenNoteIndex", index);
        startActivity(openNoteIntent);
    }
}