package com.example.lying.weightrecord.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lying.weightrecord.MyDBHelper;
import com.example.lying.weightrecord.Weight;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    //比较两个时间的大小
    public static int CompareTime(String time1,String time2){
        //时间格式为：2019-02-03(2019年2月3日)
        //前面参数(time1)大返回正数，后面参数(time2)大返回负数,相等返回0；
        if(null != time1 && null != time2 && !"".equals(time1) && !"".equals(time2) ){
            return time1.compareTo(time2);
        }
        return 0;
    }
    //把一个Weight的数据拷贝到另一个Weight
    public static Weight WeightToWeight(Weight weight){
        if(weight == null)
            return null;
        String tempData = weight.getDate();
        String tempSpecificData = weight.getSpecificDate();
        String tempSpecificTime = weight.getSpecificTime();
        String tempName = weight.getName();
        String tempWeight = weight.getWeight();
        return new Weight(tempData,tempSpecificData,tempSpecificTime,tempName,tempWeight);
    }
    //交换两个Weight中的数据
    public static void SwapWeight(Weight weight1,Weight weight2){
        String tempData = weight1.getDate();
        String tempSpecificData = weight1.getSpecificDate();
        String tempSpecificTime = weight1.getSpecificTime();
        String tempName = weight1.getName();
        String tempWeight = weight1.getWeight();
        weight1.setDate(weight2.getDate());
        weight1.setSpecificDate(weight2.getSpecificDate());
        weight1.setSpecificTime(weight2.getSpecificTime());
        weight1.setName(weight2.getName());
        weight1.setWeight(weight2.getWeight());
        weight2.setDate(tempData);
        weight2.setSpecificDate(tempSpecificData);
        weight2.setSpecificTime(tempSpecificTime);
        weight2.setName(tempName);
        weight2.setWeight(tempWeight);
    }
    //返回系统当前时间
    public static String getCurrentData(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_time = sdf.format(dt);
        return str_time;
    }
    //昨天
    private static String getYesterdayTime(){
        Calendar calendar =Calendar. getInstance();
        calendar.add( Calendar. DATE, -1); //向前走一天
        Date date= calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_time = sdf.format(date);
        return str_time;
    }
    //前天
    private static String getBeforeYesterdayTime(){
        Calendar calendar =Calendar. getInstance();
        calendar.add( Calendar. DATE, -2); //向前走两天
        Date date= calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str_time = sdf.format(date);
        return str_time;
    }
    //返回系统当前时间
    public static String getCurrentTime(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
        String str_time = sdf.format(dt);
        return str_time;
    }
    //根据时间判断是“今天”，“昨天”，“前天”
    public static String getDate(String date){
        if(getCurrentData().equals(date)){
            return "今天";
        }else if(getYesterdayTime().equals(date)){
            return "昨天";
        }else if(getBeforeYesterdayTime().equals(date)){
            return "前天";
        }else{
            return date;
        }
    }
    //通过账号获取真实姓名
    public static String getName(String UserId, SQLiteDatabase mydatabase){
        if(UserId.equals("游客")){
            return "游客";
        }
        Cursor cursor = mydatabase.rawQuery("select * from " + MyDBHelper.Table_User_NANE +" where UserID=?",new String[]{UserId} ,null);
        int count = cursor.getCount();
        if(count == 1){
            while (cursor.moveToNext()){
                String Name = cursor.getString(1);
                return Name;
            }
        }
        return "游客";
    }
    //通过账号和密码判断有没有此用户
    public static boolean haveTheUser(String UserID,String UserPW,SQLiteDatabase mydatabase){
        boolean ishave = false;
        Cursor cursor = mydatabase.rawQuery("select * from " + MyDBHelper.Table_User_NANE
                +" where UserID=? and UserPassWord=?",new String[]{UserID,UserPW} ,null);
        int count = cursor.getCount();
        cursor.close();
        if(count == 1){
            ishave = true;
        }
        return ishave;
    }
    //通过账号判断有没有此用户
    public static boolean haveTheUser(String UserID,SQLiteDatabase mydatabase){
        boolean ishave = false;
        Cursor cursor = mydatabase.rawQuery("select * from " + MyDBHelper.Table_User_NANE
                +" where UserID=?",new String[]{UserID} ,null);
        int count = cursor.getCount();
        cursor.close();
        if(count == 1){
            ishave = true;
        }
        return ishave;
    }
}
