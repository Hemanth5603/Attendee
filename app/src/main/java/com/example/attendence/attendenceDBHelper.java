package com.example.attendence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class attendenceDBHelper extends SQLiteOpenHelper {

    private static final String dbname = "database.db";

    private SQLiteDatabase db;
    private Context context;
    String TABLE_NAME = "users";

    public attendenceDBHelper(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating Table with column datatypes---
        // Table name users --
        String Q = "create table users(_id INTEGER primary key autoincrement, roll INT, Attended INTEGER DEFAULT 1)";
        db.execSQL(Q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }

    public boolean insert_data(String roll,boolean attended){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("roll",roll);
        c.put("Attended",attended);
        long r = db.insert("users",null,c);
        if(r==-1) return false;
        else return true;
    }

    public Cursor getinfo(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("select * from users",null);
        return cur;
    }


    public boolean delete_data(String roll) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where roll=?", new String[]{(roll)});
        if (cursor.getCount() > 0) {
            long r = db.delete("users", "roll=?", new String[]{roll});
            if (r == -1) return false;
            else return true;
        }else return false;
    }


    public boolean update_data(String roll,boolean check){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("Attended",check);
        Cursor cursor = db.rawQuery("select * from users where username=?",new String[]{roll});
        if(cursor.getCount()>0){
            long r = db.update("users",c,"username=?",new String[]{roll});
            if(r==-1) return false;
            else return true;
        }else return false;

    }

    @Nullable
    public ArrayList<Person> getAllPersonList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Person> personlist1 = new ArrayList<>();
        Objects.requireNonNull(personlist1);
        Cursor cursor = db.rawQuery("select * from users",null);
        boolean temp;
        if(cursor.moveToFirst()){
            do{
                int attend = 0;
                String rollno = cursor.getString(cursor.getColumnIndexOrThrow("roll"));
                attend = cursor.getInt(cursor.getColumnIndexOrThrow("Attended"));
                if(attend==0) temp=false;
                else temp = true;

                personlist1.add(new Person(rollno,temp));
            }while(cursor.moveToNext());
        }
        return personlist1;
    }

}
