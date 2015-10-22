package ru.myitschool.dummynotes;

import android.provider.BaseColumns;

/**
 * Created by teacher on 24.09.15.
 */
public final class DummyNotesContract {
    public DummyNotesContract() {}

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
    }

}
