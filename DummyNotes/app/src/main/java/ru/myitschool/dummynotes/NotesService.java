package ru.myitschool.dummynotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.myitschool.dummynotes.DummyNotesContract.NoteEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teacher on 05.10.15.
 */
public class NotesService {

    public enum SortMode { DATE, TITLE }

    private DummyNotesDbHelper mDbHelper;

    public NotesService(Context context){
        mDbHelper = new DummyNotesDbHelper(context);
    }

    public List<Note> getNotesList(SortMode sortMode){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_NAME_TITLE,
                NoteEntry.COLUMN_NAME_DATE,
        };
        String sortOrder = NoteEntry.COLUMN_NAME_DATE + " DESC";
        if (sortMode == SortMode.TITLE){
            sortOrder = NoteEntry.COLUMN_NAME_TITLE + " DESC";
        }
        Cursor cursor = db.query(
                NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<Note> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_DATE));
            result.add(new Note(id, title, date, null));
            cursor.moveToNext();
        }
        return result;
    }

}
