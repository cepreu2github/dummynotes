package ru.myitschool.dummynotes;

/**
 * Created by cepreu on 22.10.15.
 */
public class Note {

    long id;
    String title;
    String date;
    String text;

    Note(long id, String title, String date, String text){
        this.id = id;
        this.title = title;
        this.date = date;
        this.text = text;
    }

}
