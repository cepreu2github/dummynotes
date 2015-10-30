package ru.myitschool.dummynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.myitschool.dummynotes.DummyNotesContract.NoteEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Note> getNotesList(SortMode sortMode, String searchString){
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
        Cursor cursor = null;
        if (searchString.equals(""))
            cursor = db.query(
                    NoteEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        else {
            String selection = NoteEntry.COLUMN_NAME_TEXT + " LIKE ? OR " +
                    NoteEntry.COLUMN_NAME_TITLE + " LIKE ? ";
            String[] selectionArgs = {"%" + searchString + "%", "%" + searchString + "%"};
            cursor = db.query(
                    NoteEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        }
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

    public Note getNote(long id){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                NoteEntry.COLUMN_NAME_TITLE,
                NoteEntry.COLUMN_NAME_TEXT,
                NoteEntry.COLUMN_NAME_DATE,
        };
        Cursor cursor = db.query(
                NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                NoteEntry._ID + " = " + id,               // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TITLE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_DATE));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TEXT));
        return new Note(id, title, date, text);

    }

    public long saveNote(Note note){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_TITLE, note.title);
        values.put(NoteEntry.COLUMN_NAME_TEXT, note.text);
        note.date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        values.put(NoteEntry.COLUMN_NAME_DATE, note.date);
        // Insert the new row, returning the primary key value of the new row
        if (note.id == -1) {
            long newRowId;
            newRowId = db.insert(
                    NoteEntry.TABLE_NAME,
                    null,
                    values);
            note.id = newRowId;
        } else {
            String selection = NoteEntry._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(note.id) };
            db.update(
                    NoteEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        return note.id;
    }

    public void deleteNote(long id){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        String selection = NoteEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(
                NoteEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

}
