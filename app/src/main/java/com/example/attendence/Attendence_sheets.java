package com.example.attendence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

public class Attendence_sheets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_sheets);

        /*RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView show = findViewById(R.id.show);


        String Path = getIntent().getStringExtra("path");

        String temp = Path.replace("/external_files","storage/emulated/0");

        File file = new File(temp);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
        startActivity(intent);*/


        String path = getExternalFilesDir(null).getAbsolutePath();
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
        //DocumentsContract.Document.MIME_TYPE_DIR
        startActivity(intent);
        //Toast.makeText(this, Path, Toast.LENGTH_SHORT).show();
        /* File root = new File(Path);
        File[] files = root.listFiles();
        if(files==null || files.length == 0) {
            nofiles_textview.setVisibility(View.VISIBLE);
            return;
        }
        nofiles_textview.setText(View.INVISIBLE);*/

        /*Uri uri = Uri.parse(Path);
        Intent intent = new Intent(Intent.ACTION_PICK);*/
        //intent.setDataAndType(uri,"");
        //startActivity(intent);


    }
}