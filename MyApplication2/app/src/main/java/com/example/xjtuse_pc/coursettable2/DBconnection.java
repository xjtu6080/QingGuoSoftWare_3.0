package com.example.xjtuse_pc.coursettable2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBconnection extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "schedule.db";

    public DBconnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table teacher ( id_teacher text primary key,name_teacher text)";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getwriteconnection() {
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }

    public SQLiteDatabase getreadconnection() {
        SQLiteDatabase db = getReadableDatabase();
        return db;
    }

    public void close(SQLiteDatabase db) {
        db.close();
    }

}
