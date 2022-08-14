package com.example.attendence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.CellSignalStrengthGsm;
import android.util.Log;

import androidx.annotation.Nullable;

public class loginDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String dbname = "login.db";

    public loginDBHelper(@Nullable Context context) {
        super(context, dbname, null, 21);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists User (id integer primary key autoincrement, username text, email text, password text)");
        db.isOpen();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        onCreate(db);
    }


    public void registerUser(UserData data) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", data.getName());
        contentValues.put("email", data.getEmail());
        contentValues.put("password", data.getPassword());
        long user = sqLiteDatabase.insert("User", null, contentValues);
        if (user != -1) {
            Log.e(TAG, "registeruser: user Register success");
        } else {
            Log.e(TAG, "Register user failed");
        }
    }

    public Cursor getinfo(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User",null);
        return cursor;
    }
    /*public boolean insert_data(String username, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("username",username);
        c.put("email",email);
        c.put("password",password);
        long r = db.insert("users",null,c);
        if(r==-1) return false;
        else return true;
    }

    /*public void loginUser(UserData data) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User", null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);

            if (name.equals(data.getName()) && email.equals(data.getEmail()) && password.equals(data.getPassword())) {
                Log.e(TAG, "loginUser: user Login success");
            } else {
                Log.e(TAG, "loginUser: user Login failed");

            }
        }*/

    }








