package com.swufe.zbp.notebook.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.swufe.zbp.notebook.model.Note;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Note> > noteList;

    public HomeViewModel() {
        noteList = new MutableLiveData<>();
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList.setValue(noteList);
    }

    public LiveData<List<Note>> getNoteList() {
        return  noteList;
    }
}