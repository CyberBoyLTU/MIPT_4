package com.example.mipt_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initWidgets();
        checkForEditNote();
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            titleEditText.setText(selectedNote.getName());
            descEditText.setText(selectedNote.getContent());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view) {
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());

        if(inputCheck(title, desc)) {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            if(selectedNote == null)
            {
                int id = Note.noteArrayList.size();
                Note newNote = new Note(id, title, desc);
                Note.noteArrayList.add(newNote);
                sqLiteManager.addNoteToDatabase(newNote);
            }
            else
            {
                selectedNote.setTitle(title);
                selectedNote.setDescription(desc);
                sqLiteManager.updateNoteInDB(selectedNote);
            }
            finish();
        }
    }

    public boolean inputCheck(String name, String content){
        if (name.length() == 0 || content.length() == 0){
            Toast.makeText(getApplicationContext(),"Enter text first!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    public void deleteNote(View view) {
        selectedNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateNoteInDB(selectedNote);
        finish();
    }
}