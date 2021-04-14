package com.example.nsueradmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {
    private CardView selectPdf;
    private TextView pdfTitle,pdfTextView;
    private Button uploadPdfButton;

    private int REQ = 1;
    private Uri pdfData;
    private String pdfName,title;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);


        selectPdf = findViewById(R.id.selectPdf);
        pdfTitle = findViewById(R.id.pdfTitle);
        uploadPdfButton = findViewById(R.id.uploadPdfBtn);
        pdfTextView = findViewById(R.id.pdfTextView);

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("empty title");
                    pdfTitle.requestFocus();
                }else if(pdfData == null){
                    FancyToast.makeText(UploadPdf.this,"please upload pdf",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
                else {
                    uploadPdf();
                }

            }
        });
    }

    private void uploadPdf() {
        pd.setTitle("Please wait..");
        pd.setMessage("Uploading pdf");
        pd.show();
        //Upload pdf to Firebase Storage

        StorageReference reference = storageReference.child("pdf/" + pdfName +"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //To download pdf file, getting URI
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                FancyToast.makeText(UploadPdf.this,"Something went wrong",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            }
        });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("PdfTitle",title);
        data.put("pdfURL",downloadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();

                FancyToast.makeText(UploadPdf.this,"Successfully Upload",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                pdfTextView.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                FancyToast.makeText(UploadPdf.this,"Something went wrong",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("pdf/docs/ppt");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf file"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            pdfData = data.getData();
            //FancyToast.makeText(UploadPdf.this,""+pdfData, FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

            //Get pdf name
            String uriString = pdfData.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String pdfName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadPdf.this.getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                pdfName = myFile.getName();
            }

            pdfTextView.setText(pdfName);
        }
    }
}