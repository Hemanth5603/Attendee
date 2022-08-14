package com.example.attendence;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.sql.Time;

public class HomeScreen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView headerusername,headeruseremail;
    String name1 = "";
    String email1 = "";
    ImageView fileicon;
    View hview;
    Time time;
    loginDBHelper logindb;
    String password = "123";
    String path = "";
    boolean checkAct  = false;
    loginDBHelper tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeScreen.this, R.color.HomeHeader));
        getSupportActionBar().setElevation(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        Intent intent = getIntent();
        name1 = intent.getStringExtra("name1");
        email1 = intent.getStringExtra("email1");






        /*SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", "");


        if (FirstTime.equals("Yes")) {


        } else {


            logindb = new loginDBHelper(this);

            boolean check1 = logindb.insert_data(name1,email1,password);
            if(check1) Toast.makeText(HomeScreen.this, "data in database", Toast.LENGTH_SHORT).show();
            else Toast.makeText(HomeScreen.this, "no data in db", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();

        }*/


        path = getExternalFilesDir(null).getAbsolutePath(); //Getting path for download directory

        /*Cursor curr = logindb.getinfo();
        if(curr.getCount()==0) Toast.makeText(HomeScreen.this, "Not Registered Successfully", Toast.LENGTH_SHORT).show();

        while(curr.moveToNext()){
            name1 = curr.getString(1);
            email1 = curr.getString(2);
        }*/


        //Toast.makeText(this, name1, Toast.LENGTH_SHORT).show();

        hview = navigationView.getHeaderView(0);
        fileicon = findViewById(R.id.file);
        //headeruseremail = hview.findViewById(R.id.headeruseremail);
        //headerusername = hview.findViewById(R.id.headerusername);

        //headerusername.setText(name1);
        //headeruseremail.setText(email1);

        senddatatofrag(); // calling data sending function

        String folderName  = "AttendenceFolder";



        fileicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!checkpermission()){
                    Intent intent1 = new Intent(HomeScreen.this,Attendence_sheets.class);
                    // Check code wth given path ----------------------------------------------------------------
                    String path = getExternalFilesDir(Environment.getExternalStorageState()) + File.separator + folderName;
                    intent1.putExtra("path",path);
                    startActivity(intent1);
                   }else {
                    requestPermission();
                }*/

                // main code ----------------------------------------------------------------
                /*String path = getExternalFilesDir(Environment.getExternalStorageState()) + File.separator + folderName;
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_PICK);*/
                //intent.setDataAndType(uri, "*/*");
               // startActivityForResult(intent, 10);


                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,DocumentsContract.Document.MIME_TYPE_DIR);
                //DocumentsContract.Document.MIME_TYPE_DIR
                startActivity(intent);


                /*Intent intent = new Intent(Intent.ACTION_PICK);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                        +  File.separator + folderName + File.separator);
                intent.setDataAndType(uri, "Application/xls");
                startActivity(Intent.createChooser(intent, folderName));*/


            }
        });

        int i=1;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if(id==R.id.Home){
                    loadFragment(new HomeFragment());
                }
                else if(id==R.id.attendencedsheets){
                    loadFragment(new Attendence_Folder());
                    senddatatofrag1();
                }else if(id==R.id.feedback){
                    loadFragment(new FeedBack());
                }else if(id==R.id.settings){
                    Toast.makeText(HomeScreen.this, "Under Development", Toast.LENGTH_SHORT).show();
                }else if(id==R.id.Developer){
                    loadFragment(new Developer());
                }else if(id==R.id.Instructions){
                    loadFragment(new HowToUse());
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    Intent intent = new Intent(HomeScreen.this,Attendence_sheets.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();



                    /*File file = new File(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                    startActivity(intent);*/

                }
        }
    }



    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    private void senddatatofrag1(){
        Fragment fragment = new Attendence_Folder();
        Bundle bundle = new Bundle();
        bundle.putString("PATH",path);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();
    }

    private void senddatatofrag(){
        Fragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("usernames",name1);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.container,fragment);
        //new HomeFragment()
        ft.commit();

    }
    private boolean checkpermission(){
        int result = ContextCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(HomeScreen.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
        }else
        ActivityCompat.requestPermissions(HomeScreen.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
    }
}