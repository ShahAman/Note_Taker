package com.spectech.notetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Temporary code
   // Note mTempNote = new Note();

    private JSONSerializer mSerializer;
   // private List<Note> noteList = new ArrayList<>();
   private List<Note> noteList;
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private boolean mShowDividers;
    private SharedPreferences mPrefs;

    @Override
    protected void onResume(){
        super.onResume();

        mPrefs = getSharedPreferences("Note Taker",MODE_PRIVATE);
        mShowDividers = mPrefs.getBoolean("dividers",true);

        if(mShowDividers){
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        }else{
            if(recyclerView.getItemDecorationCount() > 0){
                recyclerView.removeItemDecorationAt(0);
            }
        }
    }

    public void createNewNote(Note n) {

        //mTempNote = n;
        noteList.add(n);
        mAdapter.notifyDataSetChanged();
    }

    public void showNote(int noteToShow){
        DialogShowNote dialog = new DialogShowNote();
        dialog.sendNoteSelected(noteList.get(noteToShow));
        dialog.show(getSupportFragmentManager(),"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNewNote dialog = new DialogNewNote();
                dialog.show(getSupportFragmentManager(),"");
            }
        });


        mSerializer = new JSONSerializer("NoteToSelf.json", getApplicationContext());
        try {
            noteList = mSerializer.load();
        } catch (Exception e) {
            noteList = new ArrayList<Note>();
            Log.e("Error loading notes: ", "", e);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new NoteAdapter(this, noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

   // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        recyclerView.setAdapter(mAdapter);

    }

    public void saveNotes(){
        try{
            mSerializer.save(noteList);
        }catch(Exception e){
            Log.e("Error Saving Notes","", e);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
