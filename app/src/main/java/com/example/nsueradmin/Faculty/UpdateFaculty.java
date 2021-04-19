package com.example.nsueradmin.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.nsueradmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView cseDepartment,eceDepartment,bbaDepartment,pharmacyDepartment,eteDepartment;
    private LinearLayout cseNoData,eceNoData,bbaNoData,pharmacyNoData,eteNoData;
    private List<TeacherInfo> list1,list2,list3,list4,list5;

    private DatabaseReference databaseReference,dbRef;
    private TeacherAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        cseDepartment = findViewById(R.id.cseDepartment);
        eceDepartment = findViewById(R.id.eceDepartment);
        bbaDepartment = findViewById(R.id.bbaDepartment);
        pharmacyDepartment = findViewById(R.id.pharmacyDepartment);
        eteDepartment = findViewById(R.id.eteDepartment);

        cseNoData = findViewById(R.id.cseNoData);
        eceNoData = findViewById(R.id.eceNoData);
        bbaNoData = findViewById(R.id.bbaNoData);
        pharmacyNoData = findViewById(R.id.pharmacyNoData);
        eteNoData = findViewById(R.id.eteNoData);







        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");

        cseDepartment();
        eeeDepartment();
        bbaDepartment();
        pharmacyDepartment();
        eteDepartment();


        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateFaculty.this, Addteacher.class);
                startActivity(intent);
            }
        });
    }

    private void cseDepartment() {

        dbRef = databaseReference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    cseNoData.setVisibility(View.VISIBLE);
                    cseDepartment.setVisibility(View.GONE);

                }else {
                    cseNoData.setVisibility(View.GONE);
                    cseDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherInfo data = dataSnapshot.getValue(TeacherInfo.class);
                        list1.add(data);
                    }
                    cseDepartment.hasFixedSize();
                    cseDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"CSE");
                    cseDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void eeeDepartment() {

        dbRef = databaseReference.child("EEE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if (!snapshot.exists()){
                    eceNoData.setVisibility(View.VISIBLE);
                    eceDepartment.setVisibility(View.GONE);

                }else {
                    eceNoData.setVisibility(View.GONE);
                    eceDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherInfo data = dataSnapshot.getValue(TeacherInfo.class);
                        list2.add(data);
                    }
                    eceDepartment.hasFixedSize();
                    eceDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"EEE");
                    eceDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void bbaDepartment() {

        dbRef = databaseReference.child("BBA");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    bbaNoData.setVisibility(View.VISIBLE);
                    bbaDepartment.setVisibility(View.GONE);

                }else {
                    bbaNoData.setVisibility(View.GONE);
                    bbaDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherInfo data = dataSnapshot.getValue(TeacherInfo.class);
                        list3.add(data);
                    }
                    bbaDepartment.hasFixedSize();
                    bbaDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"BBA");
                    bbaDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void pharmacyDepartment() {

        dbRef = databaseReference.child("Pharmacy");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if (!snapshot.exists()){
                    pharmacyNoData.setVisibility(View.VISIBLE);
                    pharmacyDepartment.setVisibility(View.GONE);

                }else {
                    pharmacyNoData.setVisibility(View.GONE);
                    pharmacyDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherInfo data = dataSnapshot.getValue(TeacherInfo.class);
                        list4.add(data);
                    }
                    pharmacyDepartment.hasFixedSize();
                    pharmacyDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"Pharmacy");
                    pharmacyDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void eteDepartment() {

        dbRef = databaseReference.child("ETE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();
                if (!snapshot.exists()){
                    eteNoData.setVisibility(View.VISIBLE);
                    eteDepartment.setVisibility(View.GONE);

                }else {
                    eteNoData.setVisibility(View.GONE);
                    eteDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        TeacherInfo data = dataSnapshot.getValue(TeacherInfo.class);
                        list5.add(data);
                    }
                    eteDepartment.hasFixedSize();
                    eteDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list5,UpdateFaculty.this,"ETE");
                    eteDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}