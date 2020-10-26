package com.swufe.zbp.notebook.model;

import java.util.Calendar;

public class Note {
    private String noteType;
    private String title;
    private String context;
    private int year;
    private int month;
    private int day;
    private int clock;
    private long id;
    public Note(String noteType,String title, String context,long id) {
        Calendar now = Calendar.getInstance();
        this.noteType=noteType;
        this.title=title;
        this.context=context;
        this.year = now.get(Calendar.YEAR);
        this.month = now.get(Calendar.MONTH) + 1;
        this.day=now.get(Calendar.DAY_OF_MONTH);
        this.clock = now.get(Calendar.HOUR_OF_DAY);
        this.id=id;
    }

    public Note(String noteType,String title, String context, int year, int month,int day, int clock,long id) {
        this.noteType=noteType;
        this.title = title;
        this.context = context;
        this.year = year;
        this.month = month;
        this.day=day;
        this.clock = clock;
        this.id=id;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
