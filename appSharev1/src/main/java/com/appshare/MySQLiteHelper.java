package com.appshare;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2015/5/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public MySQLiteHelper(Context context, String name, CursorFactory factory, int version){
        super(context, name, factory, version);
        //System.out.println("tag");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //System.out.println("tag");
        if(!tabbleIsExist("app_use")) {
            //db.execSQL("create table app_use(appname varchar(50),user_id varchar(50),time_use integer,time_record integer,update datetime not null default (datetime('now','localtime')));");
            db.execSQL("create table app_use(user_id varchar(50),appname varchar(50),record integer);");
            System.out.println("create table app_use");
        }
        //if(tabbleIsExist("appuse")==false) {
            //db.execSQL("create table app_use(appname varchar(50),user_id varchar(50),time_use integer,time_record integer,update datetime not null default (datetime('now','localtime')));");
          //  db.execSQL("create table appuse(appname varchar(50));");
          //  System.out.println("create table appuse");
        //}
        //else System.out.println("exit table appuse");
    }

    public boolean tabbleIsExist(String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"'; ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
