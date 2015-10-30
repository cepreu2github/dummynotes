package ru.myitschool.dummynotes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cepreu on 22.10.15.
 */
public class Note {

    long id;
    String title;
    String date;
    String text;

    Note(){
        this.id = -1;
        this.title = "";
        this.date = null;
        this.text = "";
    }

    Note(long id, String title, String date, String text){
        this.id = id;
        this.title = title;
        this.date = date;
        this.text = text;
    }

}
