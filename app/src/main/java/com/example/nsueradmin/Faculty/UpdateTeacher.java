package com.example.nsueradmin.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nsueradmin.R;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacher extends AppCompatActivity {
    private ImageView updateTeacherImage;
    private EditText updateTeacherName,updateTeacherEmail,updateTeacherPost;
    private Button updateTeacherBtn,deleteTechaerBtn;
    private String name,email,post,image;

    final int REQ=1;
    private Bitmap bitmap=null;

    private String downlaodUrl="", uniqueKey, category;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        updateTeacherBtn = findViewById(R.id.updateTeacherBtn);
        deleteTechaerBtn = findViewById(R.id.deleteTechaerBtn);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");
        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateTeacherName.setText(name);
        updateTeacherEmail.setText(email);
        updateTeacherPost.setText(post);

        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidity();
            }
        });

        deleteTechaerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daleteData();
            }
        });

    }

    private void daleteData() {
        databaseReference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FancyToast.makeText(UpdateTeacher.this,"Data delete Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FancyToast.makeText(UpdateTeacher.this,"Something went wrong",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            }
        });
    }

    private void checkValidity() {
        name = updateTeacherName.getText().toString();
        email = updateTeacherEmail.getText().toString();
        post = updateTeacherPost.getText().toString();
        
        if(name.isEmpty()){
            updateTeacherName.setText("Empty");
            updateTeacherName.requestFocus();
        }else if(email.isEmpty()){
            updateTeacherEmail.setText("Empty");
            updateTeacherEmail.requestFocus();
        }else if(post.isEmpty()){
            updateTeacherPost.setText("Empty");
            updateTeacherPost.requestFocus();
        }else if(bitmap == null){
            updateData(image);
        }else {
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();

        //Upload to firebase Storage
        final StorageReference filePath;
        filePath = storageReference.child("Teacher/").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(UpdateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downlaodUrl = String.valueOf(uri);
                                    updateData(downlaodUrl);

                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(UpdateTeacher.this,"Image Upload Falied",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {
        HashMap hashMap = new HashMap();
        hashMap.put("name",name);
        hashMap.put("email",email);
        hashMap.put("post",post);
        hashMap.put("image",s);

        databaseReference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                FancyToast.makeText(UpdateTeacher.this,"Data update Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FancyToast.makeText(UpdateTeacher.this,"Data update Failed",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

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

            updateTeacherImage.setImageBitmap(bitmap);

        }
    }
}