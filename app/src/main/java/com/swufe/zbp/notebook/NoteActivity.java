package com.swufe.zbp.notebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.swufe.zbp.notebook.dao.NoteDAO;
import com.swufe.zbp.notebook.model.CommonValue;
import com.swufe.zbp.notebook.model.Note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;


public class NoteActivity extends AppCompatActivity {
    private static String TAG="NoteActivity";
    private NoteDAO noteDAO;
    private  Spinner spinner;
    private EditText text_noteTitle;
    private EditText text_noteContext;
    private TextView label_noteId;
    private TextView label_noteTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        noteDAO=new NoteDAO(getApplicationContext());
        spinner=findViewById(R.id.spinner_types);
        text_noteTitle=findViewById(R.id.text_noteTitle);
        text_noteContext=findViewById(R.id.text_noteContext);
        label_noteId=findViewById(R.id.label_noteId);
        label_noteTime=findViewById(R.id.label_noteTime);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        Intent intent=getIntent();
        text_noteTitle.setText(intent.getStringExtra("title"));
        text_noteContext.setText(intent.getStringExtra("context"));
        label_noteId.setText(intent.getStringExtra("id"));
        label_noteTime.setText(intent.getStringExtra("time"));
        if("".equals(String.valueOf(label_noteTime.getText()))){
            label_noteTime.setText(new Date().toString());
        }


    }

    public void saveNote(View view) {
        String id=String.valueOf(label_noteId.getText());
        String title=String.valueOf(text_noteTitle.getText());
        String context=String.valueOf(text_noteContext.getText());
        String noteType = (String)spinner.getSelectedItem();
        Note note;
        Log.i(TAG, "saveNote: "+id);
        if(noteType.equals(CommonValue.ALL_THE_NOTES)){
            noteType=CommonValue.OTHERS_NOTES;
        }
        if("".equals(id)){
            SharedPreferences sharedPreferences=getSharedPreferences("notebook_setting", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            long note_id=sharedPreferences.getLong("note_id",Long.valueOf("1000000001"));
            //获取Editor
            note=new Note(noteType,title,context,note_id);
            noteDAO.add(note);
            note_id=note_id+1;
            editor.putLong("note_id",note_id);
            editor.commit();
        }else{
            note=new Note(noteType,title,context,Long.valueOf(id));
            noteDAO.update(note);
        }
        Intent intent = new Intent(NoteActivity.this,MainActivity.class);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void clearNote(View view) {
       text_noteTitle.setText("");
       text_noteContext.setText("");
       label_noteTime.setText(new Date().toString());
    }

    public void deleteNote(View view) {

        String id=String.valueOf(label_noteId.getText());
        if(!"".equals(id)){
            noteDAO.detele(Long.valueOf(id));
        }else{
            Log.i(TAG, "deleteNote: 笔记尚未保存！");
        }


        Intent intent = new Intent(NoteActivity.this,MainActivity.class);
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TAG = null;
    }

    public void goHome(View view) {
        Intent intent = new Intent(NoteActivity.this,MainActivity.class);
        setResult(RESULT_OK,intent);
        finish();
    }
}