package com.example.nsueradmin.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nsueradmin.R;
import com.example.nsueradmin.UploadNotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Addteacher extends AppCompatActivity {
    private ImageView addTecherImage;
    private EditText addTechername,addTecherEmail,addTecherPost;
    private Spinner addtecherCategory;
    private Button addtecherbtn;


    String category;
    String name,email,post,downloadUrl = "";


    final int REQ = 1;
    private ProgressDialog pd;
    private Bitmap bitmap;

    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        addTecherImage = findViewById(R.id.addTeacherImage);
        addTechername = findViewById(R.id.addTeacherName);
        addTecherEmail = findViewById(R.id.addTeacherEmail);
        addtecherCategory = findViewById(R.id.addTeacherCategory);
        addTecherPost = findViewById(R.id.addTeacherPost);
        addtecherbtn = findViewById(R.id.addTeacherBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();



        addTecherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        String[] items = new String[]{"Select Category","CSE","EEE","BBA","Pharmacy","ETE"};
        addtecherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));


        addtecherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addtecherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addtecherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidity();
            }
        });



    }

    private void checkValidity() {
        name = addTechername.getText().toString();
        email = addTecherEmail.getText().toString();
        post = addTecherPost.getText().toString();
        
        if(name.isEmpty()){
            addTechername.setError("Name is Empty");
            addTechername.requestFocus();
        }else if(email.isEmpty()){
            addTecherEmail.setError("Email is Empty");
            addTecherEmail.requestFocus();
        }else if(post.isEmpty()){
            addTecherPost.setError("Post is Empty");
            addTecherPost.requestFocus();
        }else if(category.equals("Select Category")){
            FancyToast.makeText(Addteacher.this,"Please select a teacher category",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
            
        }else if(bitmap == null){
            insertData();
        }else {
            uploadImage();
        }
    }

    private void uploadImage() {
        //Compress image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();

        //Upload to firebase Storage
        final StorageReference filePath;
        filePath = storageReference.child("Teacher/").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(Addteacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(Addteacher.this,"Image Upload Falied",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData() {
        dbRef = databaseReference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        TeacherInfo teacherInfo = new TeacherInfo(name,email,post,downloadUrl,uniqueKey);
        dbRef.child(uniqueKey).setValue(teacherInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FancyToast.makeText(Addteacher.this,"Data upload successful",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                Intent intent = new Intent(Addteacher.this,UpdateFaculty.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FancyToast.makeText(Addteacher.this,"Data upload Falied",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {

            }

            addTecherImage.setImageBitmap(bitmap);

        }
    }
}