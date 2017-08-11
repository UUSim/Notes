package com.notes;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("NoteListFragment::onCreateView enter");
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        //System.out.println("NoteListFragment::onCreateView exit");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //System.out.println("NoteListFragment::onActivityCreated");

        ArrayList<Note> notes = ((MainActivity)this.getActivity()).getNotes();
        NoteAdapter adapter = new NoteAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                notes);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
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