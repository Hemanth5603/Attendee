package com.example.attendence;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.EmbeddedData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class attendence extends AppCompatActivity {

    private RecyclerView recyclerView;
    private attendenceDBHelper dbHelper;
    Dialog dialog;
    ArrayList<Person> personlist = new ArrayList<>();
    String PATH = "";
    Date d = new Date();
    String date =  new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String Classname,Startroll,EndRoll = "";
    String PersonName = "USER";
    String Period = "0";
    int stroll,endroll,sizeofarr;
    int absentCount = 0;
    String folderName = "Attendee";
    String ClassRoll = "";
    String fileName ="";
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<String> Rollno = new ArrayList<>();
    attendenceDBHelper db = new attendenceDBHelper(attendence.this);
    private static final int PERMISSION_REQUEST_CODE = 7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        Button view = (Button) findViewById(R.id.view);
        ImageView share = (ImageView) findViewById(R.id.share);
        Button submit = (Button) findViewById(R.id.submit);
        ImageView back1 = (ImageView) findViewById(R.id.back1);

        getWindow().setStatusBarColor(ContextCompat.getColor(attendence.this,R.color.custom_blue1));
        recyclerView = findViewById(R.id.recyclerView);

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        final LoadingDialog loadingDialog = new LoadingDialog(attendence.this);
        askPermission();




        dialog = new Dialog(attendence.this);
        dialog.setContentView(R.layout.custom_opendialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);


        TextView openindialog = (TextView) dialog.findViewById(R.id.openindialog);
        TextView shareindialog = (TextView) dialog.findViewById(R.id.shareindialog);
        TextView filenamedialog = (TextView) dialog.findViewById(R.id.filenameindialog);
        recievefromHome(); // data from Home Fragment


        stroll = Integer.parseInt(Startroll);
        endroll = Integer.parseInt(EndRoll);
        sizeofarr = Math.abs(endroll-stroll);
        Classname.toUpperCase();


        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, getData(),Classname);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,0));
        recyclerView.setAdapter(recyclerViewAdapter);


        Intent intent1 = new Intent(this,HomeFragment.class);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Insertion in database
                for(int i=0;i<personlist.size();i++){
                    boolean ch = db.insert_data(personlist.get(i).getRoll(),personlist.get(i).isSelected());
                }
                //Toast.makeText(attendence.this, "Submitted Succesfully...", Toast.LENGTH_SHORT).show();


                // Calling create excel method

                if(ContextCompat.checkSelfPermission(attendence.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    createDirectory(folderName);
                }/*else{
                    askPermission();
                }*/


                // calling delete method
                deleteDate();
                filenamedialog.setText(fileName);
                dialog.show();


            }
        });
        
        openindialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(PATH);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        
        shareindialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharefile(view);
                dialog.dismiss();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharefile(view);
            }
        });




        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur = db.getinfo();
                if(cur.getCount()==0)
                    Toast.makeText(attendence.this, "No data", Toast.LENGTH_SHORT).show();
                StringBuffer buffer = new StringBuffer();
                while(cur.moveToNext()){
                    buffer.append("Roll : " + cur.getString(1)+ "\n");
                    if(cur.getInt(2)==1){
                        buffer.append("Attended :"+ "Present"+"\n");
                    }else{
                        buffer.append("Attended :"+ "Absent"+"\n");
                    }
                    // check
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(attendence.this);
                builder.setCancelable(true);
                builder.setTitle("Roll List");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });*/


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialig();
                startActivity(intent1);

                overridePendingTransition(R.anim.left_anim,R.anim.rightout_anim);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },1500);
                finish();
            }
        });


        /*openfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(PATH);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
            }
        });*/
    }


    public void askPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,@NonNull @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                createDirectory(folderName);
            }else
            {
                Toast.makeText(attendence.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory(String folderName){
        File file = new File(Environment.getExternalStorageDirectory(),folderName);

        if(!file.exists()){
            file.mkdirs();
            importData();
            Toast.makeText(this, "Succesful", Toast.LENGTH_SHORT).show();
        }
        else{
            importData();
            //Toast.makeText(this, "File already exists",Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteDate(){
        int ch=0;
        for(int i=stroll;i<=endroll;i++){
            boolean check = db.delete_data(String.valueOf(i));
            if(check) ch=1;
        }
        /*if(ch==1) Toast.makeText(attendence.this, "Deleted", Toast.LENGTH_SHORT).show();
        else Toast.makeText(attendence.this, "Cannot delete", Toast.LENGTH_SHORT).show();*/
    }

    private void importData(){
        //personlist = dbHelper.getAllPersonList();

        if(personlist.size()>0) createXLFile();
        else Toast.makeText(this, "List Empty", Toast.LENGTH_SHORT).show();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String currentDateandTime = sdf.format(new Date());

    private void createXLFile(){
        HSSFWorkbook wb = new HSSFWorkbook(); // Initially was Workbook wb **

        Cell cell = null;
        Sheet sheet = null;

        sheet = wb.createSheet("Attendence Sheet"); // Sheet Name
        Row row = sheet.createRow(0);
        Row row2 = sheet.createRow(1);

        cell = row.createCell(0);
        cell.setCellValue("Name: "+PersonName);
        //cell.setCellValue(Classname);

        cell = row.createCell(1);
        cell.setCellValue("Class: "+Classname);

        cell = row.createCell(2);
        cell.setCellValue("Period: "+Period);
        //cell.setCellValue("Date: " +date);

        cell = row.createCell(3);
        cell.setCellValue("Date: "+date);

        cell = row.createCell(4);
        cell.setCellValue("Time: "+currentDateandTime);

        cell = row2.createCell(0);
        cell.setCellValue("Roll Numbers");

        cell = row2.createCell(1);
        cell.setCellValue("Present/Absent");

        cell = row2.createCell(2);
        cell.setCellValue("Hours");




        //changeCellBackgroundColor(cell);
        sheet.setColumnWidth(0,(15 * 230));
        sheet.setColumnWidth(1,(20 * 230));
        sheet.setColumnWidth(2,(5 * 230));
        sheet.setColumnWidth(3,(15 * 230));
        sheet.setColumnWidth(4,(15 * 230));

        convertRollToClass();

        for(int i=0;i<personlist.size();i++){
            Row row1 = sheet.createRow(i+2);
            Row row3 = sheet.createRow(personlist.size()+4);
            //Row row2 = sheet.createRow(i+1);
            row1.setHeight((short) -1);
            cell = row1.createCell(0);
            if(personlist.get(i).getRoll().length()==1){
                cell.setCellValue(ClassRoll+"0"+personlist.get(i).getRoll());
            }else{
                cell.setCellValue(ClassRoll+personlist.get(i).getRoll());
            }

            cell = row1.createCell(1);
            if(personlist.get(i).isSelected()) { cell.setCellValue("present");}
            else{
                absentCount++;
                cell.setCellValue("ABSENT");


            }
            cell = row1.createCell(2);
            if(personlist.get(i).isSelected()) { cell.setCellValue("6");}
            else cell.setCellValue("0");


            cell = row3.createCell(1);
            cell.setCellValue("Absent : " + absentCount);

            sheet.setColumnWidth(0,(20 * 230));
            sheet.setColumnWidth(1,(20 * 230));
            sheet.setColumnWidth(2,(15 * 230));
        }
        fileName = "Attendence-"+ date + ".xls";
        String path = Environment.getExternalStorageDirectory() + "/Download/Attendee/" + fileName;

        //Environment.getExternalStorageDirectory() + "/Download/Attendee/"+fileName;
        //Environment.getExternalStorageDirectory()+ File.separator + folderName + File.separator + fileName;
        Log.d(TAG, path);
       // Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        PATH = path;
        File file = new File(Environment.getExternalStorageDirectory() + "/Download/Attendee/");

        //Environment.getExternalStorageDirectory() + "/Download/Attendee/");
        //       Environment.getExternalStorageDirectory(),folderName


        // Note legacy external storage is the reason for change in android 10 and 11 ----------------------------*************

        if(!file.exists()){
            if(file.mkdirs()==true);
            else Toast.makeText(this, "Cannot create directory", Toast.LENGTH_SHORT).show();
        }

        FileOutputStream outputStream = null;

        try{
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);

            //Toast.makeText(this, "Excel Created", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Cannot Create Excel", Toast.LENGTH_SHORT).show();
            try{
                outputStream.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        /*String path1 = Environment.getExternalStorageDirectory().toString() + File.separator  +folderName;
        Toast.makeText(attendence.this, path, Toast.LENGTH_SHORT).show();
        Log.d("Files: ","path"+path1);
        File directory = new File(path1);
        File[] files = directory.listFiles();
        for(int i = 0; i < files.length; i++) {
            Log.d("Files: ","FileName"+files[i].getName());
        }*/
        absentCount=0; /// attendence count bug fixed
    }

    private ArrayList<Person> getData() {
        String a = "A", b = "B", c = "C", d = "D",e = "E",f="F",g="G",h="H",I="I",j="J",k="K";
        int cnt = 0;
        for (int i = stroll; i <= endroll; i++) {
            String temp = "";
            int l = 0;
            if (i >= 100 && i < 110) {l = i % 10;temp = a+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 110 && i < 120) {l = i % 10;temp = b+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 120 && i < 130) {l = i % 10;temp = c+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 130 && i <140) {l = i % 10;temp = d+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 140 && i <150) {l = i % 10;temp = e+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 150 && i <160) {l = i % 10;temp = f+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 160 && i <170) {l = i % 10;temp = g+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 170 && i <180) {l = i % 10;temp = h+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 180 && i <190) {l = i % 10;temp = I+String.valueOf(l);arr.add(temp);cnt++;}
            else if (i >= 190 && i <200) {l = i % 10;temp = j+String.valueOf(l);arr.add(temp);cnt++;}
            else{arr.add(String.valueOf(i));cnt++;}
        }
        //Rollno[i] = arr[i];
        Rollno.addAll(arr);


        for(int i=0;i<Rollno.size();i++){
            personlist.add(new Person(Rollno.get(i),true));
        }return personlist;
    }

    public void sharefile(View view){
        File file = new File(PATH);
        if(!file.exists()){
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intentshare = new Intent(Intent.ACTION_SEND);
        intentshare.setType("application/xls");
        intentshare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        startActivity(Intent.createChooser(intentshare,"Share the file ... "));
    }

    public void openFile(){


    }

    public void recievefromHome(){
        Intent i = getIntent();
        PersonName = i.getStringExtra("PersonName");
        Classname = i.getStringExtra("classname");
        Startroll = i.getStringExtra("startroll");
        EndRoll = i.getStringExtra("endroll");
        Period = i.getStringExtra("period");
    }


    public void convertRollToClass(){

        String temp = Classname.toUpperCase();
        if(temp.indexOf("CSM")>=0) ClassRoll = "213J1A42";
        else if(temp.indexOf("CSE")>=0) ClassRoll = "213J1A05";
        else if(temp.indexOf("CSD")>=0) ClassRoll = "213J1A44";
        else if(temp.indexOf("CSC")>=0) ClassRoll = "213J1A46";
        else if(temp.indexOf("ECE")>=0) ClassRoll = "213J1A04";
        else if(temp.indexOf("MECH")>=0 || temp.indexOf("ME")>=0) ClassRoll = "213J1A03";

    }
}