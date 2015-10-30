package ru.myitschool.dummynotes;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by student on 20.10.15.
 */
public class EditFragment extends Fragment {

    private Note note;
    private boolean edited = false;

    @Override
    public void onStart() {
        super.onStart();
        // watch for changes
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                edited = true;
            }
        };
        ((EditText) getView().findViewById(R.id.nameEditText)).addTextChangedListener(textWatcher);
        ((EditText) getView().findViewById(R.id.editText)).addTextChangedListener(textWatcher);
        // set toolbar of Activity
        ((MainActivity) getActivity()).clearMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get note and show its content
        NotesService notesService = new NotesService(getActivity());
        long id = getArguments().getLong("id");
        if (id != -1)
            note = notesService.getNote(id);
        else
            note = new Note();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.edit_note, container, false);
        // place info in views
        ((EditText) rootView.findViewById(R.id.nameEditText)).setText(note.title);
        ((EditText) rootView.findViewById(R.id.editText)).setText(note.text);
        ((TextView) rootView.findViewById(R.id.dateTextView)).setText(note.date);
        // creating button for sending text
        ((Button) rootView.findViewById(R.id.sendButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                updateNoteByEdits();
                sendIntent.putExtra(Intent.EXTRA_TEXT, note.title + "\n" + note.text);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        return rootView;
    }

    private void updateNoteByEdits(){
        note.title = ((EditText) getView().findViewById(R.id.nameEditText)).getText().toString();
        note.text = ((EditText) getView().findViewById(R.id.editText)).getText().toString();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!edited)
            return;
        updateNoteByEdits();
        NotesService notesService = new NotesService(getActivity());
        notesService.saveNote(note);
        edited = false;
    }

}
