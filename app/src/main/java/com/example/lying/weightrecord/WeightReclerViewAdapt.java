package com.example.lying.weightrecord;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lying.weightrecord.util.Util;

import java.util.ArrayList;
import java.util.List;

public class WeightReclerViewAdapt extends RecyclerView.Adapter<WeightReclerViewAdapt.ViewHolder> {

    //创建ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemDate,itemSpecificDate,itemSpecificTime,itemName,itemWeight;
        public ViewHolder(View view){
            super(view);
            itemDate = (TextView)view.findViewById(R.id.itemDay);
            itemSpecificDate = (TextView)view.findViewById(R.id.itemSpecificData);
            itemSpecificTime = (TextView)view.findViewById(R.id.itemSpecificTime);
            itemName = (TextView)view.findViewById(R.id.itemName);
            itemWeight = (TextView)view.findViewById(R.id.itemWeight);
        }
    }

    private MyDBHelper myDBHelper;
    private SQLiteDatabase mydatabase;
    private Context context;
    //数据源(从数据库中获取)
    private List<Weight> Data = new ArrayList<>();

    public WeightReclerViewAdapt(Context context){
        this.context = context;
        myDBHelper=new MyDBHelper(context);
        mydatabase=myDBHelper.getWritableDatabase();
        initData();
    }

    private void initData(){
        Data.clear();
        //获取表名
        String TableName = MyDBHelper.Table_Weight_NANE;
        SharedPreferences sharedPreferences= context.getSharedPreferences("User", Context .MODE_PRIVATE);
        String userId=sharedPreferences.getString("UserID","游客");
        if(userId.equals("游客")){
            TableName = MyDBHelper.Table_Weight_NANE;
        }else{
            TableName = userId;
        }
        Cursor cursor = mydatabase.rawQuery("select * from " + TableName ,null);
        int count = cursor.getCount();
        if(count > 0){
            while (cursor.moveToNext()){
                String WeightDate = cursor.getString(1);
                String WeightSpecificDate = cursor.getString(2);
                String WeightSpecificTime = cursor.getString(3);
                String WeightName = cursor.getString(4);
                String WeightText = cursor.getString(5);
                Weight weight = new Weight(WeightDate,WeightSpecificDate,WeightSpecificTime,WeightName,WeightText);
                Data.add(weight);
            }
        }
        cursor.close();
        //如果Data中数据有 超过两个，则按时间排序
        if(Data.size() >= 2){
            for(int i=0;i<Data.size();i++){
                for(int j = i+1;j<Data.size();j++){
                    if(Util.CompareTime(Data.get(i).getSpecificDate(),Data.get(j).getSpecificDate()) < 0){
                        Util.SwapWeight(Data.get(i), Data.get(j));
                    }
                }
            }
        }
    }

    //刷新数据
    public void flashData(){
        initData();
    }

    @Override
    public WeightReclerViewAdapt.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_recyclerview_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WeightReclerViewAdapt.ViewHolder holder, int position) {
        Weight weight = Data.get(position);
        holder.itemDate.setText(Util.getDate(weight.getDate()));
        holder.itemSpecificDate.setText(weight.getSpecificDate());
        holder.itemSpecificTime.setText(weight.getSpecificTime());
        holder.itemName.setText(weight.getName());
        holder.itemWeight.setText(weight.getWeight());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
