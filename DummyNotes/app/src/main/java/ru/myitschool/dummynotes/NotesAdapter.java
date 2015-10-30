package ru.myitschool.dummynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by teacher on 24.09.15.
 */
public class NotesAdapter extends BaseAdapter {

    private Context mContext;
    private List<Note> notesList;
    private String searchString;

    LayoutInflater inflater;

    NotesAdapter(Context context, NotesService.SortMode sortMode, String searchString){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        NotesService notesService = new NotesService(context);
        notesList = notesService.getNotesList(sortMode, searchString);
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notesList.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        final int mPosition = position;
        ((TextView) view.findViewById(R.id.text_data)).setText(notesList.get(position).title);
        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesService notesService = new NotesService(mContext);
                notesService.deleteNote(notesList.get(position).id);
                notesList.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
