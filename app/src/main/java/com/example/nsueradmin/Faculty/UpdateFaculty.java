package com.example.nsueradmin.Faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nsueradmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateFaculty extends AppCompatActivity {

    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateFaculty.this,AddFaculty.class);
            }
        });
    }
}