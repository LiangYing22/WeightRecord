package com.example.lying.weightrecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String DB_NANE="database_weight_record";//数据库名
    public static final String Table_User_NANE="table_user";//用户表
    public static final String Table_Weight_NANE="table_weight";//体重表

    public MyDBHelper(Context context) {
        super(context, DB_NANE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //用户表
        db.execSQL("create table if not exists "+Table_User_NANE+"("
                +"UserID"+" interger primary key,"
                +"UserRealName"+" varchar(255),"
                +"UserPassWord"+" varchar(255)"
                +")");

        //体重表
        db.execSQL("create table if not exists "+Table_Weight_NANE+"("
                +"WeightID"+" interger primary key,"
                +"WeightDate"+" varchar(255),"
                +"WeightSpecificDate"+" varchar(255),"
                +"WeightSpecificTime"+" varchar(255),"
                +"WeightName"+" varchar(255),"
                +"WeightText"+" varchar(255)"
                +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //根据给的参数创建体重表
    public static void createWeightTable(SQLiteDatabase db,String WeightTableName){
        //体重表
        db.execSQL("create table if not exists "+WeightTableName+"("
                +"WeightID"+" interger primary key,"
                +"WeightDate"+" varchar(255),"
                +"WeightSpecificDate"+" varchar(255),"
                +"WeightSpecificTime"+" varchar(255),"
                +"WeightName"+" varchar(255),"
                +"WeightText"+" varchar(255)"
                +")");
    }
}
