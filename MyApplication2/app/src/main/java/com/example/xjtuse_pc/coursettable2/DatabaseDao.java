package com.example.xjtuse_pc.coursettable2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

/**
 * Created by XJTUSE-PC on 2017/4/18.
 */

public class DatabaseDao {
       private Map<String, String> mapTea;


    public Map<String, String> getMapTea() {
        return mapTea;
    }

    public void setMapTea(Map<String, String> mapTea) {
        this.mapTea = mapTea;
    }

    public Map<String, String> getTeacherList(DBconnection connection) {
        SQLiteDatabase db = connection.getReadableDatabase();
        String sql = "select * from teacher";
        Cursor cur = db.rawQuery(sql, null);
        while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndex("id_teacher"));
            String name = cur.getString(cur.getColumnIndex("name_teacher"));
            mapTea.put(id, name);
         /*   Log.i("ID", id);
            Log.i("name", name);*/
        }
        cur.close();
        connection.close(db);;
        return mapTea;
    }

    public Boolean find(String value) {
        for (Map.Entry<String, String> entry : mapTea.entrySet()) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String findkey(String s) {
        for (Map.Entry<String, String> entry : mapTea.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void insert(Map<String,String> teacher,DBconnection connection) {
        SQLiteDatabase db = connection.getwriteconnection();
        for(Map.Entry<String,String> entity : teacher.entrySet()) {
            String id_new = entity.getKey();
            String name_new = entity.getValue();
            mapTea.put(id_new, name_new);
            String sql = "insert into teacher (name_teacher,id_teacher) values (?,?)";
            db.execSQL(sql, new Object[]{name_new, id_new});
        }
        connection.close(db);
    }



}
