package com.spectech.notetaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private boolean mShowDrivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = getSharedPreferences("Note Taker", MODE_PRIVATE);
        mEditor = mPrefs.edit();
        mShowDrivers = mPrefs.getBoolean("dividers",true);
        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(mShowDrivers);

        switch1.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            mEditor.putBoolean("dividers",true);
                            mShowDrivers = true;
                        }else{
                            mEditor.putBoolean("dividers",false);
                            mShowDrivers = false;
                        }
                    }
                }
        );


    }



    @Override
    protected void onPause(){
        super.onPause();

        //saving the settings here
        mEditor.commit();
    }
}
