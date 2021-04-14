package com.example.nsueradmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;

public class UploadPdf extends AppCompatActivity {
    private CardView selectPdf;
    private TextView pdfTitle,pdfTextView;
    private Button uploadPdfButton;

    private int REQ = 1;
    private Uri pdfData;
    private String pdfName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

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