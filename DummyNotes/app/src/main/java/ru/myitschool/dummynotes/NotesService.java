package ru.myitschool.dummynotes;

import android.content.Context;

/**
 * Created by teacher on 05.10.15.
 */
public class NotesService {

    private DummyNotesDbHelper mDbHelper;

    public NotesService(Context context){
        mDbHelper = new DummyNotesDbHelper(context);
    }

}
