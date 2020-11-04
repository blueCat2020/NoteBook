package com.swufe.zbp.notebook.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.swufe.zbp.notebook.dao.NoteDAO;
import com.swufe.zbp.notebook.model.CommonValue;
import com.swufe.zbp.notebook.model.Note;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static String TAG="HomeViewModel";
    private NoteDAO noteDAO=null;
    private final MutableLiveData<List<Note> > noteList= new MutableLiveData<>();
    public void setNoteList(List<Note>  noteList){
        this.noteList.setValue(noteList);
    }
    public LiveData<List<Note>> getNoteList() {
        Log.i(TAG, "getNoteList: ");
        return  noteList;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public List<Note> getNotesData(String noteType){
        List<Note>  noteList=null;
        if( noteType.equals(CommonValue.ALL_THE_NOTES)){
            noteList=noteDAO.getScrollData();
        }else{
            noteList=noteDAO.getScrollData(noteType);
        }
        return noteList;
    }
    public List<Note> findByTitle(String query){
        List<Note>  noteList=null;
        noteList=noteDAO.findByTitle(query);

        return noteList;
    }
    public void deleteNote(long id){
        noteDAO.detele(id);
    }

}